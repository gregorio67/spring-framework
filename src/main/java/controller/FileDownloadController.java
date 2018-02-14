
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import taxris.framework.util.PropertiesUtil;

@Controller
public class FiledownloadCtr {
	
	@RequestMapping(value = "/sample/filedownload.do")
	public ModelAndView filedownload() throws Exception {
		ModelAndView mav = new ModelAndView("fileDownloadView");
		
		String rootDir = PropertiesUtil.getString("file.download.root.dir");
		String fileName = "TEST.txt";
		
		mav.addObject("downloadFile", rootDir + fileName);
		
		return mav;
	}

}
