package com.lp.service.edifact;

import java.util.ArrayList;
import java.util.List;

import com.lp.service.edifact.errors.EdifactException;
import com.lp.service.edifact.parser.CntSegment;
import com.lp.service.edifact.parser.ComSegment;
import com.lp.service.edifact.parser.CtaSegment;
import com.lp.service.edifact.parser.CuxSegment;
import com.lp.service.edifact.parser.DocSegment;
import com.lp.service.edifact.parser.DtmSegment;
import com.lp.service.edifact.parser.FtxSegment;
import com.lp.service.edifact.parser.ImdSegment;
import com.lp.service.edifact.parser.LinSegment;
import com.lp.service.edifact.parser.LocSegment;
import com.lp.service.edifact.parser.NadSegment;
import com.lp.service.edifact.parser.PatSegment;
import com.lp.service.edifact.parser.PiaSegment;
import com.lp.service.edifact.parser.PriSegment;
import com.lp.service.edifact.parser.QtySegment;
import com.lp.service.edifact.parser.RffSegment;
import com.lp.service.edifact.parser.SccSegment;
import com.lp.service.edifact.parser.TodSegment;
import com.lp.service.edifact.parser.UnsSegment;
import com.lp.service.edifact.schema.CntInfo;
import com.lp.service.edifact.schema.ComInfo;
import com.lp.service.edifact.schema.CtaInfo;
import com.lp.service.edifact.schema.CuxInfo;
import com.lp.service.edifact.schema.DocInfo;
import com.lp.service.edifact.schema.DtmInfo;
import com.lp.service.edifact.schema.FtxInfo;
import com.lp.service.edifact.schema.ImdInfo;
import com.lp.service.edifact.schema.LinInfo;
import com.lp.service.edifact.schema.LocInfo;
import com.lp.service.edifact.schema.NadInfo;
import com.lp.service.edifact.schema.PatInfo;
import com.lp.service.edifact.schema.PiaInfo;
import com.lp.service.edifact.schema.PriInfo;
import com.lp.service.edifact.schema.QtyInfo;
import com.lp.service.edifact.schema.RffInfo;
import com.lp.service.edifact.schema.SccInfo;
import com.lp.service.edifact.schema.TodInfo;
import com.lp.service.edifact.schema.UnsInfo;
import com.lp.service.edifact.visitor.DefaultVisitor;
import com.lp.service.edifact.visitor.Grp2Visitor;
import com.lp.service.edifact.visitor.LoggingVisitorIgnored;

public class EdifactProgramOrders96a extends EdifactProgram {
	private List<RffInfo> rffInfos = new ArrayList<RffInfo>();
	private List<DtmInfo> dtmInfos = new ArrayList<DtmInfo>();
	private List<NadInfo> nadInfos = new ArrayList<NadInfo>();
	private List<LocInfo> locInfos = new ArrayList<LocInfo>();
	private List<RffInfo> grp3RffInfos = new ArrayList<RffInfo>();
	private List<DtmInfo> grp3DtmInfos = new ArrayList<DtmInfo>();
	private List<DocInfo> grp4DocInfos = new ArrayList<DocInfo>();
	private List<DtmInfo> grp4DtmInfos = new ArrayList<DtmInfo>();
	private List<CtaInfo> grp5CtaInfos = new ArrayList<CtaInfo>();
	private List<ComInfo> grp5ComInfos = new ArrayList<ComInfo>();
	private List<CuxInfo> grp7CuxInfos = new ArrayList<CuxInfo>();
	private List<DtmInfo> grp7DtmInfos = new ArrayList<DtmInfo>();
	private List<PatInfo> grp8PatInfos = new ArrayList<PatInfo>();
	private List<DtmInfo> grp8DtmInfos = new ArrayList<DtmInfo>();
	private List<TodInfo> grp11TodInfos = new ArrayList<TodInfo>();
	private List<LocInfo> grp11LocInfos = new ArrayList<LocInfo>();
	private List<LinInfo> grp25LinInfos = new ArrayList<LinInfo>();
	private List<PiaInfo> grp25PiaInfos = new ArrayList<PiaInfo>();
	private List<ImdInfo> grp25ImdInfos = new ArrayList<ImdInfo>();
	private List<QtyInfo> grp25QtyInfos = new ArrayList<QtyInfo>();
	private List<DtmInfo> grp25DtmInfos = new ArrayList<DtmInfo>();
	private List<FtxInfo> grp25FtxInfos = new ArrayList<FtxInfo>();
	private List<PriInfo> grp28PriInfos = new ArrayList<PriInfo>();
	private List<CuxInfo> grp28CuxInfos = new ArrayList<CuxInfo>();
	private List<RffInfo> grp29RffInfos = new ArrayList<RffInfo>();
	private List<DtmInfo> grp29DtmInfos = new ArrayList<DtmInfo>();
	private List<NadInfo> grp35NadInfos = new ArrayList<NadInfo>();
	private List<LocInfo> grp35LocInfos = new ArrayList<LocInfo>();
	private List<SccInfo> grp49SccInfos = new ArrayList<SccInfo>();
	private List<FtxInfo> grp49FtxInfos = new ArrayList<FtxInfo>();
	private List<RffInfo> grp49RffInfos = new ArrayList<RffInfo>();
	private List<QtyInfo> grp50QtyInfos = new ArrayList<QtyInfo>();
	private List<DtmInfo> grp50DtmInfos = new ArrayList<DtmInfo>();
	private List<UnsInfo> endUnsInfos = new ArrayList<UnsInfo>();
	private List<CntInfo> cntInfos = new ArrayList<CntInfo>();

	private IVisitor visitor = new DefaultVisitor();
	private IVisitor grp1Visitor = new LoggingVisitorIgnored();
	private IVisitor grp2Visitor = new Grp2Visitor();
	private IVisitor grp25Visitor = new LoggingVisitorIgnored();
	private IVisitor grp29Visitor = new LoggingVisitorIgnored();
	private IVisitor grp49Visitor = new LoggingVisitorIgnored();
	

	public EdifactProgramOrders96a() {
	}
	
	@Override
	public List<EdifactOpcode<?>> getProgram(EdifactInterpreter interpreter) throws EdifactException {
		List<EdifactOpcode<?>> codes = new ArrayList<EdifactOpcode<?>>();

		EdifactGroupOpcode grp1 = new EdifactGroupOpcode("grp1", interpreter, 10, true, getGrp1Visitor())
				.add(new EdifactOpcode<RffInfo>(new RffSegment(), 1, false, rffInfos))
				.add(new EdifactOpcode<DtmInfo>(new DtmSegment(), 5, true, dtmInfos));
		codes.add(grp1);
	
		EdifactGroupOpcode grp2 = new EdifactGroupOpcode("grp2", interpreter, 20, true, getGrp2Visitor())
				.add(new EdifactOpcode<NadInfo>(new NadSegment(), 1, false, nadInfos))
				.add(new EdifactOpcode<LocInfo>(new LocSegment(), 10, true, locInfos));
	
		EdifactGroupOpcode grp3 = new EdifactGroupOpcode("grp3", interpreter, 10, true, getDefaultVisitor())
				.add(new EdifactOpcode<RffInfo>(new RffSegment(), 1, false, grp3RffInfos))
				.add(new EdifactOpcode<DtmInfo>(new DtmSegment(), 5, true, grp3DtmInfos));
		grp2.add(grp3);

		EdifactGroupOpcode grp4 = new EdifactGroupOpcode("grp4", interpreter, 5, true, getDefaultVisitor())
				.add(new EdifactOpcode<DocInfo>(new DocSegment(), 1, false, grp4DocInfos))
				.add(new EdifactOpcode<DtmInfo>(new DtmSegment(), 10, true, grp4DtmInfos));
		grp2.add(grp4);

		EdifactGroupOpcode grp5 = new EdifactGroupOpcode("grp5", interpreter, 5, true, getGrp2Visitor())
				.add(new EdifactOpcode<CtaInfo>(new CtaSegment(), 1, false, grp5CtaInfos))
				.add(new EdifactOpcode<ComInfo>(new ComSegment(), 5, true, grp5ComInfos));
		grp2.add(grp5);	
		codes.add(grp2);
		
		// Group 6 derzeit nicht implementiert - weil die betreffenden Daten nicht uebergeben werden
		// TAX MOA LOC
		
		// Group 7 CUX (PCD DTM ignoriert, weil nicht uebergeben)
		EdifactGroupOpcode grp7 = new EdifactGroupOpcode("grp7", interpreter, 5, true, getGrp2Visitor())
				.add(new EdifactOpcode<CuxInfo>(new CuxSegment(), 1, false, grp7CuxInfos))
				.add(new EdifactOpcode<DtmInfo>(new DtmSegment(), 1, true, grp7DtmInfos));
		codes.add(grp7);
		
		// Group 8 PAT (DTM PCD MOA ignoriert, weil nicht uebergeben)
		EdifactGroupOpcode grp8 = new EdifactGroupOpcode("grp8", interpreter, 10, true, getGrp2Visitor())
				.add(new EdifactOpcode<PatInfo>(new PatSegment(), 1, false, grp8PatInfos))
				.add(new EdifactOpcode<DtmInfo>(new DtmSegment(), 5, true, grp8DtmInfos));
		codes.add(grp8);
		
		// Group 9 TDT (Group 10 LOC DTM ignoriert, weil nicht uebergeben)
		
		// Group 11 TOD (LOC ignoriert weil nicht uebergeben) 
		EdifactGroupOpcode grp11 = new EdifactGroupOpcode("grp11", interpreter, 5, true, getGrp2Visitor())
				.add(new EdifactOpcode<TodInfo>(new TodSegment(), 1, false, grp11TodInfos))
				.add(new EdifactOpcode<LocInfo>(new LocSegment(), 2, true, grp11LocInfos));			
		codes.add(grp11);
		
		// Group 12 bis 24 ignoriert, weil nicht uebergeben
		
		// Group 25 LIN (MEA PCD ALI MOA GIN GIR QVR DOC PAI ignoriert, weil nicht uebergeben)
		EdifactGroupOpcode grp25 = new EdifactGroupOpcode("grp25", interpreter, 200000, true, getGrp25Visitor())
				.add(new EdifactOpcode<LinInfo>(new LinSegment(), 1, false, grp25LinInfos))
				.add(new EdifactOpcode<PiaInfo>(new PiaSegment(), 25, true, grp25PiaInfos))
				.add(new EdifactOpcode<ImdInfo>(new ImdSegment(), 99, true, grp25ImdInfos))
				.add(new EdifactOpcode<QtyInfo>(new QtySegment(), 10, true, grp25QtyInfos))
				.add(new EdifactOpcode<DtmInfo>(new DtmSegment(), 35, true, grp25DtmInfos))
				.add(new EdifactOpcode<FtxInfo>(new FtxSegment(), 99, true, grp25FtxInfos));
		codes.add(grp25);
		
		// Group 26 (CCI CAV MEA ignoriert, da nicht uebergeben)		
		// Group 27 (PAT DTM PCD MOA ignoriert, da nicht uebergeben)
		
		// Group 28 PRI (CUX APR RNG DTM ignoriert, da nicht uebergeben)
		EdifactGroupOpcode grp28 = new EdifactGroupOpcode("grp28", interpreter, 25, true, getGrp25Visitor())
				.add(new EdifactOpcode<PriInfo>(new PriSegment(), 1, false, grp28PriInfos))
				.add(new EdifactOpcode<CuxInfo>(new CuxSegment(), 1, true, grp28CuxInfos));
//		codes.add(grp28);
		grp25.add(grp28);
		
		// Group 29 RFF DTM
		EdifactGroupOpcode grp29 = new EdifactGroupOpcode("grp29", interpreter, 10, true, getGrp29Visitor())
				.add(new EdifactOpcode<RffInfo>(new RffSegment(), 1, false, grp29RffInfos))
				.add(new EdifactOpcode<DtmInfo>(new DtmSegment(), 5, true, grp29DtmInfos));
//		codes.add(grp29);
		grp25.add(grp29);
		
		// Group 30 PAC MEA QTY DTM (Group 31 RFF DTM) (Group 32 PCI RFF DTM GIN) ignoriert
		// Group 33 LOC QTY DTM ignoriert - da nicht uebergeben
		// Group 34 TAX MOA LOC ignoriert - da nicht uebergeben
		
		// Group 35 NAD (LOC (Group 36 RFF DTM) (Group 37 DOC DTM) (Group38 CTA COM) ignoriert
		EdifactGroupOpcode grp35 = new EdifactGroupOpcode("grp35", interpreter, 10, true, getGrp29Visitor())
				.add(new EdifactOpcode<NadInfo>(new NadSegment(), 1, false, grp35NadInfos))
				.add(new EdifactOpcode<LocInfo>(new LocSegment(), 5, true, grp35LocInfos));			
//		codes.add(grp35);
		grp25.add(grp35);
		
		// Group 39 - 48 ignoriert
		EdifactGroupOpcode grp49 = new EdifactGroupOpcode("grp49", interpreter, 100, true, getGrp49Visitor())
				.add(new EdifactOpcode<SccInfo>(new SccSegment(), 1, false, grp49SccInfos))
				.add(new EdifactOpcode<FtxInfo>(new FtxSegment(), 5, true, grp49FtxInfos))
				.add(new EdifactOpcode<RffInfo>(new RffSegment(), 5, true, grp49RffInfos));
//		codes.add(grp49);
		grp25.add(grp49);
		
		EdifactGroupOpcode grp50 = new EdifactGroupOpcode("grp50", interpreter, 10, true, getGrp49Visitor())
				.add(new EdifactOpcode<QtyInfo>(new QtySegment(), 1, false, grp50QtyInfos))
				.add(new EdifactOpcode<DtmInfo>(new DtmSegment(), 5, true, grp50DtmInfos));
		grp49.add(grp50);
		
		// Group 51 (RCS RFF DTM FTX) - 52 (STG Group53 (QTY MOA)) ignoriert

		codes.add(new EdifactOpcode<UnsInfo>(new UnsSegment(), 1, false, endUnsInfos));
		codes.add(new EdifactOpcode<CntInfo>(new CntSegment(), 1, true, cntInfos));

		return codes;
	}
	
	protected IVisitor getDefaultVisitor() {
		return visitor;
	}

	protected IVisitor getGrp1Visitor() {
		return grp1Visitor;
	}
		
	protected IVisitor getGrp2Visitor() {
		return grp2Visitor;
	}
	
	protected IVisitor getGrp25Visitor() {
		return grp25Visitor;
	}
	
	protected IVisitor getGrp29Visitor() {
		return grp29Visitor;
	}
	
	protected IVisitor getGrp49Visitor() {
		return grp49Visitor;
	}
	
	public List<NadInfo> getNadInfos() {
		return nadInfos;
	}
	
	public List<LinInfo> getGrp25LinInfos() {
		return grp25LinInfos;
	}
}
