package com.lp.webapp.heliumv;

import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.lp.server.system.service.KundenTelefonSuchergebnisDto;
import com.lp.server.system.service.KundenTelefonSuchergebnisDto.IdMandant;
import com.lp.server.system.service.VerfuegbareHostsDto;
import com.lp.server.util.bean.PhoneNumberBean;
import com.lp.util.Helper;

public class PhoneNumberServlet extends HttpServlet {
	private static final long serialVersionUID = 4858339846520489222L;

	private PhoneNumberBean phoneNumberBean = new PhoneNumberBean();
	
	public void init(ServletConfig config) throws ServletException {
	}
	
	@Override
	protected void doGet(HttpServletRequest req,
			HttpServletResponse resp) throws ServletException, IOException {
		String value = req.getParameter("search");
		if(!Helper.isStringEmpty(value)) {
			doSearch(value, req, resp);
			return;
		}

		value = req.getParameter("client");
		if(!Helper.isStringEmpty(value)) {
			doClientList(value, req, resp);
			return;
		}
		
		resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
	}
	
	private boolean jsonFormat(HttpServletRequest req) {
		boolean json = false;
		String contentType = req.getHeader("Accept");
		if(contentType != null && contentType.startsWith("application/json")) {
			return true;
		}
		
		json = req.getParameter("json") != null;			
		return json;
	}

	private void doSearch(String searchString, 
			HttpServletRequest req, HttpServletResponse resp) throws IOException {
		boolean modified = req.getParameter("modified") != null;
		List<KundenTelefonSuchergebnisDto> r = 
				phoneNumberBean.get().search(searchString, modified);
		try {
			if(jsonFormat(req)) {
				resp.setContentType("application/json;charset=UTF-8");
				formatJson(resp.getWriter(), searchString, r);
			} else {
				formatHtml(resp.getWriter(), searchString, r);
			}
			
			resp.setStatus(HttpServletResponse.SC_OK);
		} catch(RemoteException e) {
			resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
		}
	}

	private void doClientList(String hostName, 
			HttpServletRequest req, HttpServletResponse resp) throws IOException {
		String fromHost = req.getRemoteHost();

		List<VerfuegbareHostsDto> entries = phoneNumberBean.get().getClientList(hostName);
		if(jsonFormat(req)) {
			formatBenutzerJson(resp.getWriter(), hostName, fromHost, entries);		
		} else {
			formatBenutzerHtml(resp.getWriter(), hostName, entries);
		}
		resp.setStatus(HttpServletResponse.SC_OK);
	}
	
	private String esc(String value) {
		return "\"" + value.replace("\"", "\\\"") + "\"";
	}
	
	private void formatJson(PrintWriter out, String search, 
			List<KundenTelefonSuchergebnisDto> results) {
		out.println("{");
		out.println(esc("search") + ":" + esc(search) + ",");
		out.println(esc("entries") + ": [");
		
		boolean hasEntry = false;
		for (KundenTelefonSuchergebnisDto entry : results) {
			out.println((hasEntry ? ",{" : "{"));
			out.println(esc("partnerId") + " : " + entry.getPartnerIId() + ",");
			out.print(esc("partnerName") + " : " + esc(entry.getPartner()));
			if(entry.getAnsprechpartnerIId() != null) {
				out.println(",\n" + esc("ansprechpartnerId") + " : " + entry.getAnsprechpartnerIId() + ",");
				out.println(esc("ansprechpartnerName") + " : " + esc(entry.getAnsprechpartner()));
			}
			
			out.println("\n, " + esc("customers") + ": [");
			formatJsonIdMandant(out, entry.getKunden());
			out.println("]");
			
			out.println(", " + esc("suppliers") + ": [");
			formatJsonIdMandant(out, entry.getLieferanten());
			out.println("],");
			
			out.println(esc("hidden") + " : " + (entry.isVersteckt() ? "true" : "false"));
			
			out.println("}");
			hasEntry = true;
		}
		out.println("]");
		out.println("}");
	}
	
	private void formatJsonIdMandant(PrintWriter out, List<IdMandant> idMandants) {
		boolean hasEntry = false;
		for (IdMandant entry : idMandants) {
			out.println(hasEntry ? ",{" : "{");
			out.println(esc("entityId") + " : " + entry.getEntityId() + ",");
			out.println(esc("tenant") + " : " + esc(entry.getMandantCnr()) + "}");
			hasEntry = true;
		}
	}
	
	private void formatHtmlIdMandant(String htmlClass, PrintWriter out, List<IdMandant> idMandants) {
		if(idMandants.size() == 0) {
			out.println("<p>Es gab kein Ergebnis</p>");
		} else {
			out.println("<p>Es gab " + idMandants.size() + " Treffer:</p>");
			for (IdMandant idMandant : idMandants) {
				out.println("<div class=\"" + htmlClass + "\">");
				out.println("<p>entityId:" + idMandant.getEntityId() + 
						", Mandant: " + idMandant.getMandantCnr() + "</p>");
				out.println("</div>");
			}
		}
	}

	private void formatHtml(PrintWriter out, String search, 
			List<KundenTelefonSuchergebnisDto> results) {
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">\n"
				+ "<HTML>\n"
				+ "<HEAD><TITLE>Telefonnummernsuche</TITLE></HEAD>\n"
				+ "<BODY>\n");
		out.println("<p>Gesucht wurde nach " + search + "</p>");
		if(results.size() == 0) {
			out.println("<p>Es gab kein Ergebnis</p>");
		} else {
			out.println("<p>Es gab " + results.size() + " Treffer:</p>");
			for (KundenTelefonSuchergebnisDto entry : results) {
				out.println("<div class=\"partner\">");
				out.println("<p>partnerId:" + entry.getPartnerIId() +
							", Name: '" + entry.getPartner() + "'</p>");
				if(entry.getAnsprechpartnerIId() != null ) {
					out.println("<p>ansprechpartnerId:" + 
							entry.getAnsprechpartnerIId() + 
							", Name: '" + entry.getAnsprechpartner() + "'</p>");
				}
				out.println("<p>versteckt: " + (entry.isVersteckt() ? "ja" : "nein"));
				out.println("</div>");
				
				out.println("<div class=\"customercontainer\">");
				out.println("<p>Kunde:</p>");
				formatHtmlIdMandant("customer", out, entry.getKunden());
				out.println("</div>");
				
				out.println("<div class=\"suppliercontainer\">");
				out.println("<p>Lieferant:</p>");
				formatHtmlIdMandant("supplier", out, entry.getLieferanten());
				out.println("</div>");
			}
		}
		out.println("</BODY></HTML>");		
	}
	
	
	private void formatBenutzerHtml(PrintWriter out, String search, List<VerfuegbareHostsDto> results) {
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.0 Transitional//EN\">\n"
				+ "<HTML>\n"
				+ "<HEAD><TITLE>Benutzersuche</TITLE></HEAD>\n"
				+ "<BODY>\n");
		out.println("<p>Gesucht wurde nach Host " + search + "</p>");
		if(results.size() == 0) {
			out.println("<p>Es gab kein Ergebnis</p>");
		} else {
			out.println("<p>Es gab " + results.size() + " Treffer:</p>");
			for (VerfuegbareHostsDto entry : results) {
				out.println("<div class=\"host\">");
				out.println("<p>Mandant:" + entry.getMandantCNr() +
							", Benutzer: '" + entry.getBenutzerCNr() + "'" +
							", Port: " + (entry.getPort() + 3000) + "</p>");
				out.println("</div>");
			}
		}
		out.println("</BODY></HTML>");		
	}

	private void formatBenutzerJson(PrintWriter out, String host, String fromHost, List<VerfuegbareHostsDto> results) {
		out.println("{");
		out.println(esc("host") + ":" + esc(host) + ",");
		out.println(esc("from") + ":" + esc(fromHost) + ",");
		out.println(esc("entries") + ": [");
		
		boolean hasEntry = false;
		for (VerfuegbareHostsDto entry : results) {
			out.println((hasEntry ? ",{" : "{"));
			out.println(esc("tenant") + " : " + esc(entry.getMandantCNr()) + ",");
			out.println(esc("user") + " : " + esc(entry.getBenutzerCNr()) + ",");
			out.println(esc("port") + " : " + (entry.getPort() + 3000));
			out.println("}");
			hasEntry = true;
		}
		out.println("]");
		out.println("}");
	}
}
