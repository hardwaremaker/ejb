package com.lp.service.edifact;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.lp.server.util.logger.ILPLogger;
import com.lp.server.util.logger.LPLogService;
import com.lp.service.edifact.errors.EdifactException;
import com.lp.service.edifact.errors.InvalidSegmentException;
import com.lp.service.edifact.errors.NoProgramFoundException;
import com.lp.service.edifact.errors.NoUnhSegmentFoundException;
import com.lp.service.edifact.errors.UnexpectedProgrammEndException;
import com.lp.service.edifact.errors.UnexpectedSegmentException;
import com.lp.service.edifact.parser.BgmSegment;
import com.lp.service.edifact.parser.DtmSegment;
import com.lp.service.edifact.parser.UnbSegment;
import com.lp.service.edifact.parser.UnhSegment;
import com.lp.service.edifact.parser.UntSegment;
import com.lp.service.edifact.parser.UnzSegment;
import com.lp.service.edifact.schema.BgmInfo;
import com.lp.service.edifact.schema.DtmInfo;
import com.lp.service.edifact.schema.EdifactHeaderMessage;
import com.lp.service.edifact.schema.EdifactMessage;
import com.lp.service.edifact.schema.UnbInfo;
import com.lp.service.edifact.schema.UnhInfo;
import com.lp.service.edifact.schema.UntInfo;
import com.lp.service.edifact.schema.UnzInfo;

public class EdifactInterpreter {
	protected final ILPLogger log = LPLogService.getInstance().getLogger(EdifactGroupOpcode.class); 
	private EdifactReader reader;
	private List<EdifactOpcode<?>> ops;
	private int pc;
	private EdifactOpcode<?> lastOp;
	private EdifactMessage msg;
	private String unreadSegment;
	private Map<String, ProgramData> programs;
	
	public EdifactInterpreter(EdifactReader reader) throws EdifactException {
		this(reader, null);
	}

	public EdifactInterpreter(EdifactReader reader, List<EdifactOpcode<?>> opcodes) {
		this.reader = reader;
		ops = opcodes;
		msg = new EdifactHeaderMessage();
		programs = new HashMap<String, ProgramData>();
	}
	
	public int getPc() {
		return pc;
	}
	
	public List<EdifactOpcode<?>> getOpcodes() {
		return this.ops;
	}
	
	public void setOpcodes(List<EdifactOpcode<?>> opcodes) {
		this.ops = opcodes;
	}
	
	protected List<EdifactOpcode<?>> createOpcodes() throws EdifactException {
		List<EdifactOpcode<?>> codes = new ArrayList<EdifactOpcode<?>>();
		codes.add(new EdifactOpcode<UnbInfo>(new UnbSegment(), 1, false, msg.getUnbs()));
		codes.add(new EdifactOpcode<UnhInfo>(new UnhSegment(), 1, false, msg.getUnhs()));
		codes.add(new EdifactOpcode<BgmInfo>(new BgmSegment(), 1, false, msg.getBgms()));
		codes.add(new EdifactOpcode<DtmInfo>(new DtmSegment(), 10, false, msg.getDtms()));
		codes.add(new EdifactOpcodeLoadProgramm(this, msg.getUnhs()));
		codes.add(new EdifactOpcode<UntInfo>(new UntSegment(), 1, false, msg.getUnts()));
		codes.add(new EdifactOpcode<UnzInfo>(new UnzSegment(), 1, true, msg.getUnzs()));
		return codes;
	}
	
	public EdifactMessage interpret() throws IOException, EdifactException {
		if(ops == null || ops.size() == 0) {
			ops = createOpcodes();
		}
		
		lastOp = new OpcodeDummy();
		pc = 0;
		String segment = readFirstSegment();
		do {
			log.info("calling processIfRepetition on segment '" + segment + "' with lastop '" + lastOp.getSegmentCode() + "'." );
			boolean rc = lastOp.processIfRepetition(segment, getReader());
			log.info("called processIfRepetition on segment '" + segment + "' with lastop '" + lastOp.getSegmentCode() + "', rc=" + rc + "." );
			if(rc) {
				if(getReader().isAvailable()) {
					segment = readSegment();
					continue;					
				} else {
					break;
				}
			}

			if(pc >= ops.size()) {
				if(getReader().isSegmentSeparator()) {
					break;
				} else {
					throw new UnexpectedProgrammEndException();					
				}
			}
			
			EdifactOpcode<?> op = ops.get(pc);
			log.info("calling processNew on segment '" + segment + "' with op '" + op.getSegmentCode() + "'." );
			rc = op.processNew(segment, getReader());
			log.info("returned from calling processNew on segment '" + segment + "' with op '" + op.getSegmentCode() + "', rc=" + rc + "." );
			if(rc) {
				++pc;
				lastOp = op;
				if(getReader().isAvailable()) {
					segment = readSegment();
					continue;					
				} else {
					break;
				}
			} else {
				lastOp = new OpcodeDummy();
			}

			if(op.isMandatory()) {
				throw new UnexpectedSegmentException(segment, 
						new String[]{op.getSegmentCode()}, getReader().getStartposition(), getReader().getPosition());
			}

			++pc;
//			if(!op.isGroup()) {
//				++pc;				
//				if(getReader().isAvailable()) {
//					segment = readSegment();				
//				} else {
//					break;
//				}
//			}
		} while(true);
		
		return msg;
	}
	
	public EdifactMessage getHeaderMessage() {
		return msg;
	}
	
	protected EdifactReader getReader() {
		return reader;
	}

	public void loadUserProgram(String segment, List<UnhInfo> unhInfos) throws EdifactException {
		if(unhInfos == null || unhInfos.size() != 1) {
			throw new NoUnhSegmentFoundException();
		}
		
		ProgramData program = programs.get(unhInfos.get(0).getKeyInfo());
		if(program == null) {
			throw new NoProgramFoundException(unhInfos.get(0));
		}
		
		if(pc >= ops.size() - 1) {
			ops.addAll(program.getEdifactProgram().getProgram(this));
		} else {
			ops.addAll(pc + 1, program.getEdifactProgram().getProgram(this));			
		}
		unreadSegment(segment);
	}
	
	public void registerUserProgram(UnhInfo unhInfo, EdifactProgram program) {
		programs.put(unhInfo.getKeyInfo(), new ProgramData(unhInfo,program));
	}
	
	/**
	 * Das erste Segment beginnt entweder mit "UNA" (um Trennzeichen zu setzen)
	 * oder mit "UNH+"
	 * 
	 * @return
	 * @throws EdifactException
	 * @throws IOException
	 */
	protected String readFirstSegment() throws EdifactException, IOException {
		String segment = getReader().parseInterchange();
		Character c = read();
		if(getReader().isDataSeparator(c)) {
			return segment;
		}
		
		throw new InvalidSegmentException(segment, c);
	}
	
	public String readSegment() throws EdifactException, IOException {
		if(this.unreadSegment != null) {
			String segment = this.unreadSegment;
			unreadSegment = null;

			log.info("read unread segment '" + segment + "'");
			return segment;
		}
		
		StringBuffer sb = new StringBuffer();
		Character c = readIgnoreLineendings();
		if(getReader().isSegmentSeparator()) {
			return "";
		}
		
		sb.append(c).append(read()).append(read());
		c = read();
		if(getReader().isDataSeparator(c)) {
			log.info("read segment '" + sb.toString() + "'");
			return sb.toString();
		}
		
		throw new InvalidSegmentException(sb.toString(), c);
	}
	
	public void unreadSegment(String segment) {
		this.unreadSegment = segment;
	}
	
	private Character read() throws IOException, EdifactException {
		return getReader().read();
	}
	
	private Character readIgnoreLineendings() throws IOException, EdifactException {
		return getReader().readIgnoreLineendings();
	}
	
	public class ProgramData {
		private UnhInfo unhInfo;
		private EdifactProgram edifactProgram;

		public ProgramData() {
		}
		public ProgramData(UnhInfo unhInfo, EdifactProgram edifactProgram) {
			setUnhInfo(unhInfo);
			setEdifactProgram(edifactProgram);
		}
		
		public UnhInfo getUnhInfo() {
			return unhInfo;
		}
		public void setUnhInfo(UnhInfo unhInfo) {
			this.unhInfo = unhInfo;
		}
		public EdifactProgram getEdifactProgram() {
			return edifactProgram;
		}
		public void setEdifactProgram(EdifactProgram edifactProgram) {
			this.edifactProgram = edifactProgram;
		}		
	}
}
