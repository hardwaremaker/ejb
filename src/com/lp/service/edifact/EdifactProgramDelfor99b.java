package com.lp.service.edifact;

import java.util.ArrayList;
import java.util.List;

import com.lp.service.edifact.errors.EdifactException;
import com.lp.service.edifact.parser.ComSegment;
import com.lp.service.edifact.parser.CtaSegment;
import com.lp.service.edifact.parser.DtmSegment;
import com.lp.service.edifact.parser.FtxSegment;
import com.lp.service.edifact.parser.GisSegment;
import com.lp.service.edifact.parser.ImdSegment;
import com.lp.service.edifact.parser.LinSegment;
import com.lp.service.edifact.parser.LocSegment;
import com.lp.service.edifact.parser.NadSegment;
import com.lp.service.edifact.parser.PiaSegment;
import com.lp.service.edifact.parser.QtySegment;
import com.lp.service.edifact.parser.RffSegment;
import com.lp.service.edifact.parser.SccSegment;
import com.lp.service.edifact.schema.ComInfo;
import com.lp.service.edifact.schema.CtaInfo;
import com.lp.service.edifact.schema.DtmInfo;
import com.lp.service.edifact.schema.FtxInfo;
import com.lp.service.edifact.schema.GisInfo;
import com.lp.service.edifact.schema.ImdInfo;
import com.lp.service.edifact.schema.LinInfo;
import com.lp.service.edifact.schema.LocInfo;
import com.lp.service.edifact.schema.NadInfo;
import com.lp.service.edifact.schema.PiaInfo;
import com.lp.service.edifact.schema.QtyInfo;
import com.lp.service.edifact.schema.RffInfo;
import com.lp.service.edifact.schema.SccInfo;
import com.lp.service.edifact.visitor.DefaultVisitor;
import com.lp.service.edifact.visitor.Sg12Visitor;
import com.lp.service.edifact.visitor.Sg22Visitor;
import com.lp.service.edifact.visitor.Sg27Visitor;
import com.lp.service.edifact.visitor.Sg2Visitor;
import com.lp.service.edifact.visitor.Sg7Visitor;

public class EdifactProgramDelfor99b extends EdifactProgram {
	private List<FtxInfo> ftxInfos;
	private List<RffInfo> rffInfos;
	private List<DtmInfo> dtmInfos;
	private List<NadInfo> nadInfos;
	
	private List<RffInfo> sg3RffInfos;
	private List<DtmInfo> sg3DtmInfos;
	private List<GisInfo> gisInfos;

	private List<CtaInfo> sg4CtaInfos;
	private List<ComInfo> sg4ComInfos;
	
	private List<NadInfo> sg7NadInfos;
	private List<LocInfo> sg7LocInfos;
	private List<FtxInfo> sg7FtxInfos;

	private List<LinInfo> linInfos;
	private List<DtmInfo> sg12DtmInfos;
	private List<FtxInfo> sg12FtxInfos;
	private List<PiaInfo> sg12PiaInfos;
	private List<ImdInfo> sg12ImdInfos;
	private List<LocInfo> sg12LocInfos;

	private List<RffInfo> sg13RffInfos;
	private List<DtmInfo> sg13DtmInfos;

	private List<QtyInfo> sg15QtyInfos;
	private List<DtmInfo> sg15DtmInfos;
	
	private List<RffInfo> sg16RffInfos;
	private List<DtmInfo> sg16DtmInfos;
	
	private List<SccInfo> sg17SccInfos;
	
	private List<QtyInfo> sg18QtyInfos;
	private List<DtmInfo> sg18DtmInfos;

	private List<RffInfo> sg19RffInfos;
	private List<DtmInfo> sg19DtmInfos;

	private List<NadInfo> sg22NadInfos;
	private List<LocInfo> sg22LocInfos;
	private List<FtxInfo> sg22FtxInfos;

	private List<CtaInfo> sg24CtaInfos;
	private List<ComInfo> sg24ComInfos;

	private List<QtyInfo> sg25QtyInfos;
	private List<DtmInfo> sg25DtmInfos;
	
	private List<RffInfo> sg26RffInfos;
	private List<DtmInfo> sg26DtmInfos;
	
	private List<SccInfo> sg27SccInfos;

	private List<QtyInfo> sg28QtyInfos;
	private List<DtmInfo> sg28DtmInfos;

	private List<RffInfo> sg29RffInfos;
	private List<DtmInfo> sg29DtmInfos;

	private IVisitor visitor = new DefaultVisitor();
	private IVisitor sg2Visitor = new Sg2Visitor();
	private IVisitor sg7Visitor = new Sg7Visitor();
	private IVisitor sg12Visitor = new Sg12Visitor();
	private IVisitor sg22Visitor = new Sg22Visitor();
	private IVisitor sg27Visitor = new Sg27Visitor();
	

	public EdifactProgramDelfor99b() {
		ftxInfos = new ArrayList<FtxInfo>();
		rffInfos = new ArrayList<RffInfo>();
		dtmInfos = new ArrayList<DtmInfo>();
		nadInfos = new ArrayList<NadInfo>();
		sg3RffInfos = new ArrayList<RffInfo>();
		sg3DtmInfos = new ArrayList<DtmInfo>();
		sg4CtaInfos = new ArrayList<CtaInfo>();
		sg4ComInfos = new ArrayList<ComInfo>();
		gisInfos = new ArrayList<GisInfo>();
		sg7NadInfos = new ArrayList<NadInfo>();
		sg7LocInfos = new ArrayList<LocInfo>();
		sg7FtxInfos = new ArrayList<FtxInfo>();
		linInfos = new ArrayList<LinInfo>();
		sg12DtmInfos = new ArrayList<DtmInfo>();
		sg12FtxInfos = new ArrayList<FtxInfo>();
		sg12PiaInfos = new ArrayList<PiaInfo>();
		sg12ImdInfos = new ArrayList<ImdInfo>();
		sg12LocInfos = new ArrayList<LocInfo>();
		sg13RffInfos = new ArrayList<RffInfo>();
		sg13DtmInfos = new ArrayList<DtmInfo>();
		sg15QtyInfos = new ArrayList<QtyInfo>();
		sg15DtmInfos = new ArrayList<DtmInfo>();
		sg16RffInfos = new ArrayList<RffInfo>();
		sg16DtmInfos = new ArrayList<DtmInfo>();
		sg17SccInfos = new ArrayList<SccInfo>();
		sg18QtyInfos = new ArrayList<QtyInfo>();
		sg18DtmInfos = new ArrayList<DtmInfo>();
		sg19RffInfos = new ArrayList<RffInfo>();
		sg19DtmInfos = new ArrayList<DtmInfo>();
		sg22NadInfos = new ArrayList<NadInfo>();
		sg22LocInfos = new ArrayList<LocInfo>();
		sg22FtxInfos = new ArrayList<FtxInfo>();
		sg24CtaInfos = new ArrayList<CtaInfo>();
		sg24ComInfos = new ArrayList<ComInfo>();
		sg25QtyInfos = new ArrayList<QtyInfo>();
		sg25DtmInfos = new ArrayList<DtmInfo>();
		sg26RffInfos = new ArrayList<RffInfo>();
		sg26DtmInfos = new ArrayList<DtmInfo>();
		sg27SccInfos = new ArrayList<SccInfo>();
		sg28QtyInfos = new ArrayList<QtyInfo>();
		sg28DtmInfos = new ArrayList<DtmInfo>();
		sg29RffInfos = new ArrayList<RffInfo>();
		sg29DtmInfos = new ArrayList<DtmInfo>();
	}
	
	protected IVisitor getDefaultVisitor() {
		return visitor;
	}
	
	protected IVisitor getSg2Visitor() {
		return sg2Visitor;
	}
	
	protected IVisitor getSg7Visitor() {
		return sg7Visitor;
	}

	protected IVisitor getSg12Visitor() {
		return sg12Visitor;
	}

	protected IVisitor getSg22Visitor() {
		return sg22Visitor;
	}
	
	protected IVisitor getSg27Visitor() {
		return sg27Visitor;
	}

	
	@Override
	public List<EdifactOpcode<?>> getProgram(EdifactInterpreter interpreter) throws EdifactException {
		List<EdifactOpcode<?>> codes = new ArrayList<EdifactOpcode<?>>();
		codes.add(new EdifactOpcode<FtxInfo>(new FtxSegment(), 5, true, ftxInfos, getDefaultVisitor()));
		
		EdifactGroupOpcode sg1 = new EdifactGroupOpcode("sg1", interpreter, 10, true, getDefaultVisitor())
			.add(new EdifactOpcode<RffInfo>(new RffSegment(), 1, false, rffInfos))
			.add(new EdifactOpcode<DtmInfo>(new DtmSegment(), 1, true, dtmInfos));
		codes.add(sg1);
		
		EdifactGroupOpcode sg2 = new EdifactGroupOpcode("sg2", interpreter, 99, true, getSg2Visitor())
			.add(new EdifactOpcode<NadInfo>(new NadSegment(), 1, false, nadInfos));
		codes.add(sg2);
		
		EdifactGroupOpcode sg3 = new EdifactGroupOpcode("sg3", interpreter, 10, true, getDefaultVisitor())
			.add(new EdifactOpcode<RffInfo>(new RffSegment(), 1, false, sg3RffInfos))
			.add(new EdifactOpcode<DtmInfo>(new DtmSegment(), 1, true, sg3DtmInfos));
		sg2.add(sg3);

		EdifactGroupOpcode sg4 = new EdifactGroupOpcode("sg4", interpreter, 5, true, getDefaultVisitor())
			.add(new EdifactOpcode<CtaInfo>(new CtaSegment(), 1, false, sg4CtaInfos))
			.add(new EdifactOpcode<ComInfo>(new ComSegment(), 5, true, sg4ComInfos));
		sg2.add(sg4);
		
		EdifactGroupOpcode sg6 = new EdifactGroupOpcode("sg6", interpreter, 9999, true, getDefaultVisitor())
			.add(new EdifactOpcode<GisInfo>(new GisSegment(), 1, false, gisInfos));
		codes.add(sg6);
	
		EdifactGroupOpcode sg7 = new EdifactGroupOpcode("sg7", interpreter, 1, true, getSg7Visitor())
			.add(new EdifactOpcode<NadInfo>(new NadSegment(), 1, false, sg7NadInfos))
			.add(new EdifactOpcode<LocInfo>(new LocSegment(), 10, true, sg7LocInfos))
			.add(new EdifactOpcode<FtxInfo>(new FtxSegment(), 5, true, sg7FtxInfos));
		sg6.add(sg7);
		
		EdifactGroupOpcode sg12 = new EdifactGroupOpcode("sg12", interpreter, 9999, true, getSg12Visitor())
			.add(new EdifactOpcode<LinInfo>(new LinSegment(), 1, false, linInfos))
			.add(new EdifactOpcode<PiaInfo>(new PiaSegment(), 10, true, sg12PiaInfos))
			.add(new EdifactOpcode<ImdInfo>(new ImdSegment(), 10, true, sg12ImdInfos))
			.add(new EdifactOpcode<LocInfo>(new LocSegment(), 999, true, sg12LocInfos))			
			.add(new EdifactOpcode<DtmInfo>(new DtmSegment(), 5, true, sg12DtmInfos))
			.add(new EdifactOpcode<FtxInfo>(new FtxSegment(), 5, true, sg12FtxInfos));
		sg6.add(sg12);

		EdifactGroupOpcode sg13 = new EdifactGroupOpcode("sg13", interpreter, 10, true, getSg12Visitor())
			.add(new EdifactOpcode<RffInfo>(new RffSegment(), 1, false, sg13RffInfos))
			.add(new EdifactOpcode<DtmInfo>(new DtmSegment(), 1, true, sg13DtmInfos));
		sg12.add(sg13);
	
		EdifactGroupOpcode sg15 = new EdifactGroupOpcode("sg15", interpreter, 99, true, getDefaultVisitor())
			.add(new EdifactOpcode<QtyInfo>(new QtySegment(), 1, false, sg15QtyInfos))
			.add(new EdifactOpcode<DtmInfo>(new DtmSegment(), 2, true, sg15DtmInfos));
		sg12.add(sg15);

		EdifactGroupOpcode sg16 = new EdifactGroupOpcode("sg16", interpreter, 10, true, getDefaultVisitor())
			.add(new EdifactOpcode<RffInfo>(new RffSegment(), 1, false, sg16RffInfos))
			.add(new EdifactOpcode<DtmInfo>(new DtmSegment(), 1, true, sg16DtmInfos));
		sg15.add(sg16);
		
		EdifactGroupOpcode sg17 = new EdifactGroupOpcode("sg17", interpreter, 999, true, getDefaultVisitor())
			.add(new EdifactOpcode<SccInfo>(new SccSegment(), 1, false, sg17SccInfos)) ;
		sg12.add(sg17);

		EdifactGroupOpcode sg18 = new EdifactGroupOpcode("sg18", interpreter, 999, true, getDefaultVisitor())
			.add(new EdifactOpcode<QtyInfo>(new QtySegment(), 1, false, sg18QtyInfos))
			.add(new EdifactOpcode<DtmInfo>(new DtmSegment(), 2, true, sg18DtmInfos));
		sg17.add(sg18);

		EdifactGroupOpcode sg19 = new EdifactGroupOpcode("sg19", interpreter, 10, true, getDefaultVisitor())
			.add(new EdifactOpcode<RffInfo>(new RffSegment(), 1, false, sg19RffInfos))
			.add(new EdifactOpcode<DtmInfo>(new DtmSegment(), 1, true, sg19DtmInfos));
		sg18.add(sg19);

		EdifactGroupOpcode sg22 = new EdifactGroupOpcode("sg22", interpreter, 999, true, getSg22Visitor())
			.add(new EdifactOpcode<NadInfo>(new NadSegment(), 1, false, sg22NadInfos))
			.add(new EdifactOpcode<LocInfo>(new LocSegment(), 10, true, sg22LocInfos))
			.add(new EdifactOpcode<FtxInfo>(new FtxSegment(), 5, true, sg22FtxInfos));
		sg12.add(sg22);
		
		EdifactGroupOpcode sg24 = new EdifactGroupOpcode("sg24", interpreter, 5, true, getSg22Visitor())
			.add(new EdifactOpcode<CtaInfo>(new CtaSegment(), 1, false, sg24CtaInfos))
			.add(new EdifactOpcode<ComInfo>(new ComSegment(), 5, true, sg24ComInfos));
		sg22.add(sg24);
		EdifactGroupOpcode sg25 = new EdifactGroupOpcode("sg25", interpreter, 10, true, getSg22Visitor())
			.add(new EdifactOpcode<QtyInfo>(new QtySegment(), 1, false, sg25QtyInfos))
			.add(new EdifactOpcode<DtmInfo>(new DtmSegment(), 2, true, sg25DtmInfos));
		sg22.add(sg25);
		EdifactGroupOpcode sg26 = new EdifactGroupOpcode("sg26", interpreter, 10, true, getSg22Visitor())
			.add(new EdifactOpcode<RffInfo>(new RffSegment(), 1, false, sg26RffInfos))
			.add(new EdifactOpcode<DtmInfo>(new DtmSegment(), 1, true, 	sg26DtmInfos));
		sg25.add(sg26);
		
		EdifactGroupOpcode sg27 = new EdifactGroupOpcode("sg27", interpreter, 999, false, getSg27Visitor())
			.add(new EdifactOpcode<SccInfo>(new SccSegment(), 1, false, sg27SccInfos)) ;
		sg22.add(sg27);
		
		EdifactGroupOpcode sg28 = new EdifactGroupOpcode("sg28", interpreter, 999, false, getSg27Visitor())
			.add(new EdifactOpcode<QtyInfo>(new QtySegment(), 1, false, sg28QtyInfos))
			.add(new EdifactOpcode<DtmInfo>(new DtmSegment(), 2, true, sg28DtmInfos));
		sg27.add(sg28);
		
		EdifactGroupOpcode sg29 = new EdifactGroupOpcode("sg29", interpreter, 10, true, getSg27Visitor())
			.add(new EdifactOpcode<RffInfo>(new RffSegment(), 1, false, sg29RffInfos))
			.add(new EdifactOpcode<DtmInfo>(new DtmSegment(), 1, true, 	sg29DtmInfos));
		sg28.add(sg29);
		
		return codes;
	}
	
	public List<RffInfo> getRffInfos() {
		return rffInfos;
	}
	public List<DtmInfo> getDtmInfos() {
		return dtmInfos;
	}
	public List<NadInfo> getNadInfos() {
		return nadInfos;
	}
	public List<GisInfo> getGisInfos() {
		return gisInfos;
	}
	public List<LinInfo> getLinInfos() {
		return linInfos;
	}
	public List <CtaInfo> getSg4CtayInfos() {
		return sg4CtaInfos;
	}
	public List <ComInfo> getSg4ComInfos() {
		return sg4ComInfos;
	}
	public List<DtmInfo> getSg12DtmInfos() {
		return sg12DtmInfos;
	}
	public List<FtxInfo> getSg12FtxInfos() {
		return sg12FtxInfos;
	}
	public List <QtyInfo> getSg15QtyInfos() {
		return sg15QtyInfos;
	}
	public List <DtmInfo> getSg15DtmInfos() {
		return sg15DtmInfos;
	}
	public List <SccInfo> getSg17SccInfos() {
		return sg17SccInfos;
	}
	public List <QtyInfo> getSg18QtyInfos() {
		return sg18QtyInfos;
	}
	public List <DtmInfo> getSg18DtmInfos() {
		return sg18DtmInfos;
	}
	public List<RffInfo> getSg19RffInfos() {
		return sg19RffInfos;
	}
	public List<DtmInfo> getSg19DtmInfos() {
		return sg19DtmInfos;
	}
	public List <NadInfo> getSg22NadInfos() {
		return sg22NadInfos;
	}
	public List <LocInfo> getSg22LocInfos() {
		return sg22LocInfos;
	}
	public List <FtxInfo> getSg22FtxInfos() {
		return sg22FtxInfos;
	}	
	public List <CtaInfo> getSg24CtayInfos() {
		return sg24CtaInfos;
	}
	public List <ComInfo> getSg24ComInfos() {
		return sg24ComInfos;
	}
	public List <QtyInfo> getSg25QtyInfos() {
		return sg25QtyInfos;
	}
	public List <DtmInfo> getSg25DtmInfos() {
		return sg25DtmInfos;
	}
	public List<RffInfo> getSg26RffInfos() {
		return sg26RffInfos;
	}
	public List<DtmInfo> getSg26DtmInfos() {
		return sg26DtmInfos;
	}
	public List<SccInfo> getSg27SccInfos() {
		return sg27SccInfos;
	}	
	public List <QtyInfo> getSg28QtyInfos() {
		return sg28QtyInfos;
	}
	public List <DtmInfo> getSg28DtmInfos() {
		return sg28DtmInfos;
	}
	public List<RffInfo> getSg29RffInfos() {
		return sg29RffInfos;
	}
	public List<DtmInfo> getSg29DtmInfos() {
		return sg29DtmInfos;
	}	
}
