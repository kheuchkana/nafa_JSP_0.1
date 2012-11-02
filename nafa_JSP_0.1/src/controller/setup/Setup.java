package controller.setup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import modele.dao.HibernateSessionFactory;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.support.RequestContextUtils;

@Controller
public class Setup {

/*	public void handleRequestInternal (HttpServletRequest request, HttpServletResponse response) {
		LocaleResolver localeResolver = RequestContextUtils.getLocaleResolver(request);
		Locale locale = localeResolver.resolveLocale(request);
		System.out.println("1_"+locale.toString());

		Locale newLocale = new Locale("en_US");
		localeResolver.setLocale(request, response, newLocale);
		System.out.println("2_"+newLocale.toString()+"\n");
	}*/
	
	@RequestMapping (method={RequestMethod.GET, RequestMethod.POST}, value="/install")
	public ModelAndView install (HttpServletRequest request, HttpServletResponse response) {
		
		ModelAndView mav = new ModelAndView();
		String step = null;
		
		List<String> steps = new ArrayList<String>();
		steps.add("install"); steps.add("choixLangue"); steps.add("precheck"); 
		steps.add("license"); steps.add("infosBD");		steps.add("adminInfos");
		steps.add("installSuccess");
		
		boolean pass = true;
		
		if (request.getParameter("step")!=null) {
			step = request.getParameter("step");
			mav.addObject("step", step);
		}

		Locale locale = (RequestContextUtils.getLocaleResolver(request)).resolveLocale(request);
		mav.addObject("lang", locale.toString());
		
		if (request.getParameter("lang")!=null) {
			String lang = request.getParameter("lang");
			mav.addObject("lang", lang);
		}
		
		if ((request.getParameter("precheckServerInfoState")!=null && request.getParameter("precheckServerInfoState").equals("notOK")) || 
				(request.getParameter("precheckServerVersionState")!=null && request.getParameter("precheckServerVersionState").equals("notOK")) ||
				(request.getParameter("precheckJavaVersionState")!=null && request.getParameter("precheckJavaVersionState").equals("notOK"))) {
			pass = false;
			mav.addObject("precheckErrorMsg", "precheckErrorMsg");
		}
		
		if (request.getParameter("bd")!=null) {
			
			String bd = request.getParameter("bd");
			mav.addObject("bd", bd);
			
			if ((request.getParameter("bdHost")==null || request.getParameter("bdHost").equals("") || 
					request.getParameter("bdUser")==null || request.getParameter("bdUser").equals("") ||
					request.getParameter("bdName")==null || request.getParameter("bdName").equals("") || 
					request.getParameter("bdPort")==null || request.getParameter("bdPort").equals("")) && (step.equals("infosBD")))
				pass = false;
			
			else {
				
				String bdUser = request.getParameter("bdUser"); 		mav.addObject("bdUser", bdUser);
				String bdHost = request.getParameter("bdHost"); 		mav.addObject("bdHost", bdHost);
				String bdPort = request.getParameter("bdPort"); 		mav.addObject("bdPort", bdPort);
				String bdName = request.getParameter("bdName"); 		mav.addObject("bdName", bdName);
				String bdUserMdp = request.getParameter("bdUserMdp");	mav.addObject("bdUserMdp", bdUserMdp);
				
				if (step.equals("infosBD")) {
					updateHibernateCfgFile(bd, bdHost, bdPort, bdName, bdUser, bdUserMdp);
					Session s = HibernateSessionFactory.getSession();
					if (s.isConnected()) {
						System.out.println("--- Connexion reussie ---");
						s.close();
					}
					else
						System.out.println("--- Connexion echouee ---");
				}
			}
			
/*			if (request.getParameter("bdHost")!=null)
				mav.addObject("bdHost", request.getParameter("bdHost"));
			
			if (request.getParameter("bdUser")!=null) 
				mav.addObject("bdUser", request.getParameter("bdUser"));

			if (request.getParameter("bdUserMdp")!=null)
				mav.addObject("bdUserMdp", request.getParameter("bdUserMdp"));
*/
		}
		
		if (request.getParameter("action")!=null) {
//			step = request.getParameter("step");
						
			if (request.getParameter("action").equals("next") || request.getParameter("action").equals("accept")) {
				step = pass==true ? steps.get(steps.indexOf(step)+1) : steps.get(steps.indexOf(step));
//				System.out.println(pass);
			}
			if (request.getParameter("action").equals("previous")) {
				step = steps.get(steps.indexOf(step)-1);
			}
			if (request.getParameter("action").equals("not_accept")) {	
				mav.addObject("erreur_licence","erreur_licence");
			}
			if (request.getParameter("action").equals("end")) {	
				step = steps.get(steps.indexOf(step)+1); 
			}
			mav.addObject("step",step);
		}
		
		if (request.getParameter("theme")!=null) {
			mav.addObject("theme", request.getParameter("theme"));
			System.out.println("---> "+request.getParameter("theme"));
		}
		
		
		
		mav.setViewName("install");
		return mav;
	}
	
	private void updateHibernateCfgFile (String bd, String bdHost, String bdPort, String bdName, String bdUser, String bdUserMdp) {
		
		System.out.println("------------------------------ Creation hibernate config file -----------------------------");		
		
		try {
			
			URL resource = Thread.currentThread().getContextClassLoader().getResource("hibernate.cfg.xml");
			File f = new File(resource.toURI());
			
			SAXReader xmlReader = new SAXReader();
			Document doc = xmlReader.read(f);
			Element sessionFactory = (Element) doc.getRootElement().elements().get(0);
			
			if (sessionFactory.elements().size()!=0)		
				for (Iterator elt = sessionFactory.elements().iterator(); elt.hasNext(); )
					sessionFactory.remove((Element) elt.next());			
			
			if (bd.equals("mysql")) {
				sessionFactory.addElement("property").addAttribute("name", "dialect").addText("org.hibernate.dialect.MySQLDialect");
				sessionFactory.addElement("property").addAttribute("name", "connection.url").addText("jdbc:mysql://"+bdHost+":"+bdPort+"/"+bdName);
				sessionFactory.addElement("property").addAttribute("name", "connection.driver_class").addText("com.mysql.jdbc.Driver");
			}
			else if (bd.equals("pgsql")) {
				sessionFactory.addElement("property").addAttribute("name", "dialect").addText("org.hibernate.dialect.PostgreSQLDialect");
				sessionFactory.addElement("property").addAttribute("name", "connection.url").addText("jdbc:postgresql://"+bdHost+":"+bdPort+"/"+bdName);
				sessionFactory.addElement("property").addAttribute("name", "connection.driver_class").addText("org.postgresql.Driver");
			}
			else if (bd.equals("oracle")) {
				sessionFactory.addElement("property").addAttribute("name", "dialect").addText("org.hibernate.dialect.OracleDialect");
				sessionFactory.addElement("property").addAttribute("name", "connection.url").addText("jdbc:oracle:thin:@"+bdHost+":"+bdPort+":"+bdName);
				sessionFactory.addElement("property").addAttribute("name", "connection.driver_class").addText("oracle.jdbc.OracleDriver");
			}
			else if (bd.equals("mssqlserver")) {
				sessionFactory.addElement("property").addAttribute("name", "dialect").addText("org.hibernate.dialect.SQLServerDialect");
				sessionFactory.addElement("property").addAttribute("name", "connection.url").addText("jdbc:sqlserver://"+bdHost+":"+bdPort+";databaseName="+bdName+";");			
				sessionFactory.addElement("property").addAttribute("name", "connection.driver_class").addText("com.microsoft.sqlserver.jdbc.SQLServerDriver");
			}
			
			sessionFactory.addElement("property").addAttribute("name", "connection.username").addText(bdUser);
			sessionFactory.addElement("property").addAttribute("name", "connection.password").addText(bdUserMdp);
			sessionFactory.addElement("property").addAttribute("name", "hbm2ddl.auto").addText("update");
		
			XMLWriter writer = new XMLWriter(new FileWriter(f));
			writer.write(doc);
			writer.close();			
								
			System.out.println("------------------------------ Creation hibernate config file over -----------------------------");
			
		}
		
		catch (URISyntaxException e) {
			e.printStackTrace();
		}
		
		catch (DocumentException e) {
			e.printStackTrace();
		}
		
		catch (IOException e) {
			e.printStackTrace();
		}
			
	}

	
	@RequestMapping (method={RequestMethod.GET,RequestMethod.POST}, value="/choixLangue")
	public ModelAndView choixLangue (/*@RequestParam String lang, */HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mav = new ModelAndView();
		if (request.getParameter("lang")!=null) {
			String lang = request.getParameter("lang");
			mav.addObject(lang);
		}
		mav.setViewName("choixLangue");
		return mav;
	}
	
//	@RequestMapping ("/choixLangue")
//	public ModelAndView choixLangue () {
//		ModelAndView mav = new ModelAndView();
//		mav.setViewName("test");
//		return mav;
//	}
//	
}
