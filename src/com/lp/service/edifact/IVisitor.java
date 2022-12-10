package com.lp.service.edifact;

import com.lp.service.edifact.parser.BgmSegment;
import com.lp.service.edifact.parser.CntSegment;
import com.lp.service.edifact.parser.ComSegment;
import com.lp.service.edifact.parser.CtaSegment;
import com.lp.service.edifact.parser.CuxSegment;
import com.lp.service.edifact.parser.DocSegment;
import com.lp.service.edifact.parser.DtmSegment;
import com.lp.service.edifact.parser.FtxSegment;
import com.lp.service.edifact.parser.GisSegment;
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
import com.lp.service.edifact.parser.TdtSegment;
import com.lp.service.edifact.parser.TodSegment;
import com.lp.service.edifact.parser.UnbSegment;
import com.lp.service.edifact.parser.UnhSegment;
import com.lp.service.edifact.parser.UnsSegment;
import com.lp.service.edifact.parser.UntSegment;
import com.lp.service.edifact.parser.UnzSegment;


public interface IVisitor {
	void visit(UnbSegment unbSegment);
	void visit(BgmSegment bgmSegment);
	void visit(CntSegment cntSegment);
	void visit(ComSegment comSegment);
	void visit(CtaSegment ctaSegment);
	void visit(CuxSegment cuxSegment);
	void visit(DocSegment docSegment);
	void visit(DtmSegment dtmSegment);
	void visit(FtxSegment ftxSegment);
	void visit(GisSegment gisSegment);
	void visit(ImdSegment imdSegment);
	void visit(LinSegment linSegment);
	void visit(LocSegment locSegment);
	void visit(NadSegment nadSegment);
	void visit(PatSegment patSegment);
	void visit(PiaSegment piaSegment);
	void visit(PriSegment priSegment);
	void visit(QtySegment qtySegment);
	void visit(RffSegment rffSegment);
	void visit(SccSegment sccSegment);
	void visit(TdtSegment tdtSegment);
	void visit(TodSegment todSegment);	
	void visit(UnhSegment unhSegment);
	void visit(UnsSegment unsSegment);
	void visit(UntSegment untSegment);
	void visit(UnzSegment unzSegment);
}
