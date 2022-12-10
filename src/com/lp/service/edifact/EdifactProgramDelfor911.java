package com.lp.service.edifact;

import java.util.ArrayList;
import java.util.List;

import com.lp.service.edifact.errors.EdifactException;
import com.lp.service.edifact.parser.CntSegment;
import com.lp.service.edifact.parser.ComSegment;
import com.lp.service.edifact.parser.CtaSegment;
import com.lp.service.edifact.parser.DocSegment;
import com.lp.service.edifact.parser.DtmSegment;
import com.lp.service.edifact.parser.FtxSegment;
import com.lp.service.edifact.parser.ImdSegment;
import com.lp.service.edifact.parser.LinSegment;
import com.lp.service.edifact.parser.LocSegment;
import com.lp.service.edifact.parser.NadSegment;
import com.lp.service.edifact.parser.PiaSegment;
import com.lp.service.edifact.parser.QtySegment;
import com.lp.service.edifact.parser.RffSegment;
import com.lp.service.edifact.parser.SccSegment;
import com.lp.service.edifact.parser.TdtSegment;
import com.lp.service.edifact.parser.UnsSegment;
import com.lp.service.edifact.schema.CntInfo;
import com.lp.service.edifact.schema.ComInfo;
import com.lp.service.edifact.schema.CtaInfo;
import com.lp.service.edifact.schema.DocInfo;
import com.lp.service.edifact.schema.DtmInfo;
import com.lp.service.edifact.schema.FtxInfo;
import com.lp.service.edifact.schema.ImdInfo;
import com.lp.service.edifact.schema.LinInfo;
import com.lp.service.edifact.schema.LocInfo;
import com.lp.service.edifact.schema.NadInfo;
import com.lp.service.edifact.schema.PiaInfo;
import com.lp.service.edifact.schema.QtyInfo;
import com.lp.service.edifact.schema.RffInfo;
import com.lp.service.edifact.schema.SccInfo;
import com.lp.service.edifact.schema.TdtInfo;
import com.lp.service.edifact.schema.UnsInfo;
import com.lp.service.edifact.visitor.DefaultVisitor;
import com.lp.service.edifact.visitor.Grp11Visitor;
import com.lp.service.edifact.visitor.Grp12Visitor;
import com.lp.service.edifact.visitor.Grp17Visitor;
import com.lp.service.edifact.visitor.Grp20Visitor;
import com.lp.service.edifact.visitor.Grp23Visitor;
import com.lp.service.edifact.visitor.Grp2Visitor;
import com.lp.service.edifact.visitor.Grp4Visitor;
import com.lp.service.edifact.visitor.Grp7Visitor;
import com.lp.service.edifact.visitor.Grp8Visitor;

public class EdifactProgramDelfor911 extends EdifactProgram {
	private List<RffInfo> rffInfos = new ArrayList<RffInfo>();
	private List<DtmInfo> dtmInfos = new ArrayList<DtmInfo>();
	private List<NadInfo> nadInfos = new ArrayList<NadInfo>();
	private List<LocInfo> locInfos = new ArrayList<LocInfo>();
	private List<CtaInfo> grp3CtaInfos = new ArrayList<CtaInfo>();
	private List<ComInfo> grp3ComInfos = new ArrayList<ComInfo>();
	private List<UnsInfo> unsInfos = new ArrayList<UnsInfo>();
	private List<UnsInfo> endUnsInfos = new ArrayList<UnsInfo>();
	private List<NadInfo> grp4NadInfos = new ArrayList<NadInfo>();
	private List<LocInfo> grp4LocInfos = new ArrayList<LocInfo>();
	private List<FtxInfo> grp4FtxInfos = new ArrayList<FtxInfo>();
	private List<DocInfo> grp4DocInfos = new ArrayList<DocInfo>();
	private List<DtmInfo> grp4DtmInfos = new ArrayList<DtmInfo>();
	private List<CtaInfo> grp4CtaInfos = new ArrayList<CtaInfo>();
	private List<ComInfo> grp4ComInfos = new ArrayList<ComInfo>();
	private List<TdtInfo> grp7TdtInfos = new ArrayList<TdtInfo>();
	private List<ComInfo> grp7ComInfos = new ArrayList<ComInfo>();
	private List<LinInfo> linInfos = new ArrayList<LinInfo>();
	private List<DtmInfo> grp8DtmInfos = new ArrayList<DtmInfo>();
	private List<FtxInfo> grp8FtxInfos = new ArrayList<FtxInfo>();
	private List<PiaInfo> grp8PiaInfos = new ArrayList<PiaInfo>();
	private List<ImdInfo> grp8ImdInfos = new ArrayList<ImdInfo>();
	private List<LocInfo> grp8LocInfos = new ArrayList<LocInfo>();
	private List<RffInfo> grp9RffInfos = new ArrayList<RffInfo>();
	private List<DtmInfo> grp9DtmInfos = new ArrayList<DtmInfo>();
	private List<TdtInfo> grp10TdtInfos = new ArrayList<TdtInfo>();
	private List<DtmInfo> grp10DtmInfos = new ArrayList<DtmInfo>();
	private List<QtyInfo> grp11QtyInfos = new ArrayList<QtyInfo>();
	private List<SccInfo> grp11SccInfos = new ArrayList<SccInfo>();
	private List<DtmInfo> grp11DtmInfos = new ArrayList<DtmInfo>();
	private List<RffInfo> grp12RffInfos = new ArrayList<RffInfo>();
	private List<DtmInfo> grp12DtmInfos = new ArrayList<DtmInfo>();
	private List<LinInfo> grp17LinInfos = new ArrayList<LinInfo>();
	private List<DtmInfo> grp17DtmInfos = new ArrayList<DtmInfo>();
	private List<FtxInfo> grp17FtxInfos = new ArrayList<FtxInfo>();
	private List<PiaInfo> grp17PiaInfos = new ArrayList<PiaInfo>();
	private List<ImdInfo> grp17ImdInfos = new ArrayList<ImdInfo>();
	private List<RffInfo> grp18RffInfos = new ArrayList<RffInfo>();
	private List<DtmInfo> grp18DtmInfos = new ArrayList<DtmInfo>();
	private List<QtyInfo> grp19QtyInfos = new ArrayList<QtyInfo>();
	private List<SccInfo> grp19SccInfos = new ArrayList<SccInfo>();
	private List<DtmInfo> grp19DtmInfos = new ArrayList<DtmInfo>();
	private List<RffInfo> grp20RffInfos = new ArrayList<RffInfo>();
	private List<DtmInfo> grp20DtmInfos = new ArrayList<DtmInfo>();
	private List<NadInfo> grp23NadInfos = new ArrayList<NadInfo>();
	private List<LocInfo> grp23LocInfos = new ArrayList<LocInfo>();
	private List<FtxInfo> grp23FtxInfos = new ArrayList<FtxInfo>();
	private List<DocInfo> grp23DocInfos = new ArrayList<DocInfo>();
	private List<DtmInfo> grp23DtmInfos = new ArrayList<DtmInfo>();
	private List<CtaInfo> grp23CtaInfos = new ArrayList<CtaInfo>();
	private List<ComInfo> grp23ComInfos = new ArrayList<ComInfo>();
	private List<QtyInfo> grp26QtyInfos = new ArrayList<QtyInfo>();
	private List<SccInfo> grp26SccInfos = new ArrayList<SccInfo>();
	private List<DtmInfo> grp26DtmInfos = new ArrayList<DtmInfo>();
	private List<FtxInfo> ftxInfos = new ArrayList<FtxInfo>();
	private List<CntInfo> cntInfos = new ArrayList<CntInfo>();
	
	private IVisitor visitor = new DefaultVisitor();
	private IVisitor grp2Visitor = new Grp2Visitor();
	private IVisitor grp4Visitor = new Grp4Visitor();
	private IVisitor grp7Visitor = new Grp7Visitor();
	private IVisitor grp8Visitor = new Grp8Visitor();
	private IVisitor grp11Visitor = new Grp11Visitor();
	private IVisitor grp12Visitor = new Grp12Visitor();
	private IVisitor grp17Visitor = new Grp17Visitor();
	private IVisitor grp20Visitor = new Grp20Visitor();
	private IVisitor grp23Visitor = new Grp23Visitor();
	
	public EdifactProgramDelfor911() {
	}
	
	@Override
	public List<EdifactOpcode<?>> getProgram(EdifactInterpreter interpreter)
			throws EdifactException {
		List<EdifactOpcode<?>> codes = new ArrayList<EdifactOpcode<?>>();
		
		EdifactGroupOpcode grp1 = new EdifactGroupOpcode("grp1", interpreter, 10, true, getDefaultVisitor())
			.add(new EdifactOpcode<RffInfo>(new RffSegment(), 1, false, rffInfos))
			.add(new EdifactOpcode<DtmInfo>(new DtmSegment(), 1, true, dtmInfos));
		codes.add(grp1);
		
		EdifactGroupOpcode grp2 = new EdifactGroupOpcode("grp2", interpreter, 20, true, getGrp2Visitor())
			.add(new EdifactOpcode<NadInfo>(new NadSegment(), 1, false, nadInfos))
			.add(new EdifactOpcode<LocInfo>(new LocSegment(), 10, true, locInfos));
		codes.add(grp2);

		EdifactGroupOpcode grp3 = new EdifactGroupOpcode("grp3", interpreter, 5, true, getDefaultVisitor())
			.add(new EdifactOpcode<CtaInfo>(new CtaSegment(), 1, false, grp3CtaInfos))
			.add(new EdifactOpcode<ComInfo>(new ComSegment(), 5, true, grp3ComInfos));
		grp2.add(grp3);
		
		codes.add(new EdifactOpcode<UnsInfo>(new UnsSegment(), 1, false, unsInfos));
		
		EdifactGroupOpcode grp4 = new EdifactGroupOpcode("grp4", interpreter, 500, true, getGrp4Visitor())
			.add(new EdifactOpcode<NadInfo>(new NadSegment(), 1, false, grp4NadInfos))
			.add(new EdifactOpcode<LocInfo>(new LocSegment(), 10, true, grp4LocInfos))
			.add(new EdifactOpcode<FtxInfo>(new FtxSegment(), 5, true, grp4FtxInfos));
		codes.add(grp4);

		EdifactGroupOpcode grp5 = new EdifactGroupOpcode("grp5", interpreter, 10, true, getGrp4Visitor())
			.add(new EdifactOpcode<DocInfo>(new DocSegment(), 1, false, grp4DocInfos))
			.add(new EdifactOpcode<DtmInfo>(new DtmSegment(), 10, true, grp4DtmInfos));
		grp4.add(grp5);

		EdifactGroupOpcode grp6 = new EdifactGroupOpcode("grp6", interpreter, 5, true, getGrp4Visitor())
			.add(new EdifactOpcode<CtaInfo>(new CtaSegment(), 1, false, grp4CtaInfos))
			.add(new EdifactOpcode<ComInfo>(new ComSegment(), 5, true, grp4ComInfos));
		grp4.add(grp6);

		EdifactGroupOpcode grp7 = new EdifactGroupOpcode("grp7", interpreter, 10, true, getGrp7Visitor())
			.add(new EdifactOpcode<TdtInfo>(new TdtSegment(), 1, false, grp7TdtInfos))
			.add(new EdifactOpcode<ComInfo>(new ComSegment(), 5, true, grp7ComInfos));
		grp4.add(grp7);

//		EdifactGroupOpcode grp8 = new EdifactGroupOpcode("grp8", interpreter, 200, true, getGrp8Visitor())
// Umstellung auf 9999 Eintraege, da bisher auch mal 201 geliefert wurden.
// Vermutlich ist das so gedacht, dass bei einer Ueberschreitung der 
// Wiederholungen von GRP8 auf GRP17 gewechselt werden soll.
		EdifactGroupOpcode grp8 = new EdifactGroupOpcode("grp8", interpreter, 9999, true, getGrp8Visitor())
			.add(new EdifactOpcode<LinInfo>(new LinSegment(), 1, false, linInfos))
			.add(new EdifactOpcode<PiaInfo>(new PiaSegment(), 10, true, grp8PiaInfos))
			.add(new EdifactOpcode<ImdInfo>(new ImdSegment(), 10, true, grp8ImdInfos))
			.add(new EdifactOpcode<LocInfo>(new LocSegment(), 100, true, grp8LocInfos))			
			.add(new EdifactOpcode<DtmInfo>(new DtmSegment(), 5, true, grp8DtmInfos))
			.add(new EdifactOpcode<FtxInfo>(new FtxSegment(), 5, true, grp8FtxInfos));
		grp4.add(grp8);
	
		EdifactGroupOpcode grp9 = new EdifactGroupOpcode("grp9", interpreter, 10, true, getGrp8Visitor())
			.add(new EdifactOpcode<RffInfo>(new RffSegment(), 1, false, grp9RffInfos))
			.add(new EdifactOpcode<DtmInfo>(new DtmSegment(), 1, true,  grp9DtmInfos));
		grp8.add(grp9);

		EdifactGroupOpcode grp10 = new EdifactGroupOpcode("grp10", interpreter, 10, true, getGrp8Visitor())
			.add(new EdifactOpcode<TdtInfo>(new TdtSegment(), 1, false, grp10TdtInfos))
			.add(new EdifactOpcode<DtmInfo>(new DtmSegment(), 1, true,  grp10DtmInfos));
		grp8.add(grp10);

		EdifactGroupOpcode grp11 = new EdifactGroupOpcode("grp11", interpreter, 200, true, getGrp11Visitor())
			.add(new EdifactOpcode<QtyInfo>(new QtySegment(), 1, false, grp11QtyInfos))
			.add(new EdifactOpcode<SccInfo>(new SccSegment(), 1, true, grp11SccInfos))
			.add(new EdifactOpcode<DtmInfo>(new DtmSegment(), 2, true, grp11DtmInfos));
		grp8.add(grp11);

		EdifactGroupOpcode grp12 = new EdifactGroupOpcode("grp12", interpreter, 10, true, getGrp12Visitor())
				.add(new EdifactOpcode<RffInfo>(new RffSegment(), 1, false, grp12RffInfos))
				.add(new EdifactOpcode<DtmInfo>(new DtmSegment(), 1, true, grp12DtmInfos));
		grp11.add(grp12);
		
		EdifactGroupOpcode grp17 = new EdifactGroupOpcode("grp17", interpreter, 9999, true, getGrp17Visitor())
			.add(new EdifactOpcode<LinInfo>(new LinSegment(), 1, false, grp17LinInfos))
			.add(new EdifactOpcode<PiaInfo>(new PiaSegment(), 10, true, grp17PiaInfos))
			.add(new EdifactOpcode<ImdInfo>(new ImdSegment(), 10, true, grp17ImdInfos))
			.add(new EdifactOpcode<DtmInfo>(new DtmSegment(), 5, true, grp17DtmInfos))
			.add(new EdifactOpcode<FtxInfo>(new FtxSegment(), 5, true, grp17FtxInfos));
		grp4.add(grp17);
		
		EdifactGroupOpcode grp18 = new EdifactGroupOpcode("grp18", interpreter, 10, true, getGrp17Visitor())
			.add(new EdifactOpcode<RffInfo>(new RffSegment(), 1, false, grp18RffInfos))
			.add(new EdifactOpcode<DtmInfo>(new DtmSegment(), 1, true,  grp18DtmInfos));
		grp17.add(grp18);

		EdifactGroupOpcode grp19 = new EdifactGroupOpcode("grp19", interpreter, 50, true, getGrp17Visitor())
			.add(new EdifactOpcode<QtyInfo>(new QtySegment(), 1, false, grp19QtyInfos))
			.add(new EdifactOpcode<SccInfo>(new SccSegment(), 1, true, grp19SccInfos))
			.add(new EdifactOpcode<DtmInfo>(new DtmSegment(), 2, true, grp19DtmInfos));
		grp17.add(grp19);
		
		EdifactGroupOpcode grp20 = new EdifactGroupOpcode("grp20", interpreter, 10, true, getGrp20Visitor())
			.add(new EdifactOpcode<RffInfo>(new RffSegment(), 1, false, grp20RffInfos))
			.add(new EdifactOpcode<DtmInfo>(new DtmSegment(), 1, true,  grp20DtmInfos));
		grp19.add(grp20);
	
		EdifactGroupOpcode grp23 = new EdifactGroupOpcode("grp23", interpreter, 500, true, getGrp23Visitor())
			.add(new EdifactOpcode<NadInfo>(new NadSegment(), 1, false, grp23NadInfos))
			.add(new EdifactOpcode<LocInfo>(new LocSegment(), 10, true, grp23LocInfos))
			.add(new EdifactOpcode<FtxInfo>(new FtxSegment(), 5, true, grp23FtxInfos));
		grp17.add(grp23);

		EdifactGroupOpcode grp24 = new EdifactGroupOpcode("grp24", interpreter, 10, true, getGrp23Visitor())
			.add(new EdifactOpcode<DocInfo>(new DocSegment(), 1, false, grp23DocInfos))
			.add(new EdifactOpcode<DtmInfo>(new DtmSegment(), 10, true, grp23DtmInfos));
		grp23.add(grp24);

		EdifactGroupOpcode grp25 = new EdifactGroupOpcode("grp25", interpreter, 5, true, getGrp23Visitor())
			.add(new EdifactOpcode<CtaInfo>(new CtaSegment(), 1, false, grp23CtaInfos))
			.add(new EdifactOpcode<ComInfo>(new ComSegment(), 5, true, grp23ComInfos));
		grp23.add(grp25);

		EdifactGroupOpcode grp26 = new EdifactGroupOpcode("grp26", interpreter, 50, true, getGrp23Visitor())
			.add(new EdifactOpcode<QtyInfo>(new QtySegment(), 1, false, grp26QtyInfos))
			.add(new EdifactOpcode<SccInfo>(new SccSegment(), 1, true, grp26SccInfos))
			.add(new EdifactOpcode<DtmInfo>(new DtmSegment(), 2, true, grp26DtmInfos));
		grp23.add(grp26);
	
		codes.add(new EdifactOpcode<UnsInfo>(new UnsSegment(), 1, false, endUnsInfos));
		codes.add(new EdifactOpcode<CntInfo>(new CntSegment(), 1, true, cntInfos));
		codes.add(new EdifactOpcode<FtxInfo>(new FtxSegment(), 5, true, ftxInfos));
		
		return codes;
	}
	
	public List<NadInfo> getNadInfos() {
		return nadInfos;
	}
	
	protected IVisitor getDefaultVisitor() {
		return visitor;
	}
	
	protected IVisitor getGrp2Visitor() {
		return grp2Visitor;
	}

	protected IVisitor getGrp4Visitor() {
		return grp4Visitor;
	}

	protected IVisitor getGrp7Visitor() {
		return grp7Visitor;
	}

	protected IVisitor getGrp8Visitor() {
		return grp8Visitor;
	}

	protected IVisitor getGrp11Visitor() {
		return grp11Visitor;
	}
	
	protected IVisitor getGrp12Visitor() {
		return grp12Visitor;
	}

	protected IVisitor getGrp17Visitor() {
		return grp17Visitor;
	}

	protected IVisitor getGrp20Visitor() {
		return grp20Visitor;
	}

	protected IVisitor getGrp23Visitor() {
		return grp23Visitor;
	}
}
