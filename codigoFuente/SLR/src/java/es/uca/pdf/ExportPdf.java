package es.uca.pdf;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.itextpdf.text.Chapter;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Section;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import es.uca.pfc.ResearchQuestion;
import es.uca.pfc.Slr;
import es.uca.pfc.SpecificAttribute;
import es.uca.pfc.SpecificAttributeMultipleValue;
import es.uca.pfc.UserProfile;

public class ExportPdf {
	
	public static Font coverFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 30, Font.BOLD);
	public static Font headerFont = FontFactory.getFont(FontFactory.HELVETICA, 18, Font.BOLD);
	public static Font normalFont = FontFactory.getFont(FontFactory.HELVETICA, 12);
	public static Font underlineFont = FontFactory.getFont(FontFactory.HELVETICA, 12, Font.UNDERLINE);
	public static Font tableFont = FontFactory.getFont(FontFactory.HELVETICA, 10);
	
	public static void createPdf(String filePdf, String fileLogoUCA, Slr slrInstance, UserProfile userProfile, List<ResearchQuestion> questions,
			List<SpecificAttribute> attributes, List<SearchHelper> searchs, List<CriterionStudyHelper> criterions, 
			List<PrimaryStudyHelper> primaryStudies, Map<String, AnnualTrend> annualTrends)
	{
		Document document = new Document();
		try
		{			
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(filePdf));
			document.open();
			
			addFrontPage(fileLogoUCA, document, slrInstance, userProfile);
			addContentPdf(document, slrInstance, questions, attributes, searchs, criterions, primaryStudies, annualTrends);
			addAttributesPdf(document, slrInstance, userProfile);
			
			document.close();
			writer.close();
			
		}
		catch(Exception ex)
		{
			ex.printStackTrace();
		}
	}
	
	private static void addFrontPage(String fileLogoUCA, Document document, Slr slrInstance, UserProfile userProfile) throws MalformedURLException, IOException, DocumentException
	{
		//Image imageCover = Image.getInstance("templates/logoUCA.PNG");
		Image imageCover = Image.getInstance(fileLogoUCA);
		imageCover.scaleAbsolute(200, 300);
		imageCover.setAlignment(Image.ALIGN_CENTER);
		document.add(imageCover);
		
		Paragraph title = new Paragraph("SLR: " + slrInstance.getTitle(), coverFont);
		title.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(title);
		
		Paragraph author = new Paragraph(userProfile.getDisplay_name(), headerFont);
		author.setAlignment(Paragraph.ALIGN_CENTER);
		document.add(author);
	}
	
	private static void addContentPdf(Document document, Slr slrInstance, List<ResearchQuestion> questions, 
			List<SpecificAttribute> attributes, List<SearchHelper> searchs, List<CriterionStudyHelper> criterions,
			List<PrimaryStudyHelper> primaryStudies, Map<String, AnnualTrend> annualTrends) throws DocumentException
	{
		document.newPage();
		
		Paragraph chapterTitle = new Paragraph("SLR: " + slrInstance.getTitle(), headerFont);
		Chapter chapter = new Chapter(chapterTitle, 1);
		chapter.setNumberDepth(0);
		
		addSection1(chapter, slrInstance);
		addSection2(chapter, slrInstance, questions);
		addSection3(chapter, slrInstance, attributes);
		addSection4(chapter, slrInstance, searchs);
		addSection5(chapter, slrInstance, criterions);
		addSection6(chapter, primaryStudies);
		addSection7(chapter, annualTrends);
		document.add(chapter);
	}

	private static void addAttributesPdf(Document document, Slr slrInstance, UserProfile userProfile)
	{
		document.addAuthor(userProfile.getDisplay_name());
		document.addCreationDate();
		document.addCreator("Systematic Literature Review");
		document.addTitle("SLR: " + slrInstance.getTitle());
	}
	
	private static void addSection1(Chapter chapter, Slr slrInstance)
	{
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		
		Paragraph sectionTitle = new Paragraph("Index", headerFont);
		sectionTitle.setSpacingBefore(10);
		Section section = chapter.addSection(sectionTitle);
		section.setIndentationLeft(10);
		
		Chunk chunk01 = new Chunk("Title", underlineFont);
		Chunk chunk02 = new Chunk(": " + slrInstance.getTitle(), normalFont);
		Paragraph p = new Paragraph();
		p.add(chunk01); p.add(chunk02);
		p.setIndentationLeft(10);
		p.setSpacingBefore(10);
		section.add(p);
		
		chunk01 = new Chunk("Create date", underlineFont);
		chunk02 = new Chunk(": " + df.format(slrInstance.getSubmitDate()), normalFont);
		p = new Paragraph();
		p.setSpacingBefore(10);
		p.setIndentationLeft(10);
		p.add(chunk01); p.add(chunk02);
		section.add(p);

		chunk01 = new Chunk("Justification", underlineFont);
		chunk02 = new Chunk(": " + slrInstance.getJustification(), normalFont);
		p = new Paragraph();
		p.setSpacingBefore(10);
		p.setIndentationLeft(10);
		p.add(chunk01); p.add(chunk02);
		section.add(p);
	}
	
	private static void addSection2(Chapter chapter, Slr slrInstance, List<ResearchQuestion> questions) throws DocumentException
	{
		Paragraph sectionTitle = new Paragraph("Research Questions", headerFont);
		sectionTitle.setSpacingBefore(10);
		Section section = chapter.addSection(sectionTitle);
		section.setIndentationLeft(10);
		
		Chunk chunk01 = new Chunk("Total", underlineFont);
		Chunk chunk02 = new Chunk(": " + questions.size(), normalFont);
		Paragraph p = new Paragraph();
		p.add(chunk01); p.add(chunk02);
		p.setIndentationLeft(10);
		p.setSpacingBefore(10);
		section.add(p);
		
		PdfPTable table = new PdfPTable(2);
		table.setWidthPercentage(100); //Width 100%
        table.setSpacingBefore(10f); //Space before table
        table.setSpacingAfter(10f); //Space after table
        
        //Set Column widths
        float[] columnWidths = {0.2f, 1.7f};
        table.setWidths(columnWidths);
        
        PdfPCell cell = getHeaderCell("#");
        table.addCell(cell);
        
        cell = getHeaderCell("Research Question");
        table.addCell(cell);
        
        int cont = 1;
        for(ResearchQuestion question : questions)
        {
        	cell = getCell(Integer.toString(cont));        	
            table.addCell(cell);
            
        	cell = getCell(question.getEnunciado());
            table.addCell(cell);

            cont++;
        }
        
        section.add(table);
	}
	
	private static void addSection3(Chapter chapter, Slr slrInstance, List<SpecificAttribute> attributes) throws DocumentException
	{
		Paragraph sectionTitle = new Paragraph("Specific Attributes", headerFont);
		sectionTitle.setSpacingBefore(10);
		Section section = chapter.addSection(sectionTitle);
		section.setIndentationLeft(10);
		
		Chunk chunk01 = new Chunk("Total", underlineFont);
		Chunk chunk02 = new Chunk(": " + attributes.size(), normalFont);
		Paragraph p = new Paragraph();
		p.add(chunk01); p.add(chunk02);
		p.setIndentationLeft(10);
		p.setSpacingBefore(10);
		section.add(p);
		
		PdfPTable table = new PdfPTable(4);
		table.setWidthPercentage(100); //Width 100%
        table.setSpacingBefore(10f); //Space before table
        table.setSpacingAfter(10f); //Space after table
        
        //Set Column widths
        float[] columnWidths = {0.2f, 1.8f, 0.8f, 1.2f};
        table.setWidths(columnWidths);
        
        PdfPCell cell = getHeaderCell("#");
        table.addCell(cell);
        
        cell = getHeaderCell("Name");
        table.addCell(cell);
        
        cell = getHeaderCell("Type");
        table.addCell(cell);
        
        cell = getHeaderCell("Values");
        table.addCell(cell);
        
        int cont = 1;
        for(SpecificAttribute attribute : attributes)
        {	
        	cell = getCell(Integer.toString(cont));
            table.addCell(cell);
            
        	cell = getCell(attribute.getName());
            table.addCell(cell);
            
        	cell = getCell(attribute.getTipo());
            table.addCell(cell);

            String options = "--";
            if (attribute instanceof SpecificAttributeMultipleValue)
            {
            	SpecificAttributeMultipleValue aSpec = (SpecificAttributeMultipleValue) attribute;
            	options = aSpec.getStrOptions();
            }
        	cell = getCell(options);
            table.addCell(cell);
            cont++;
        }
        
        section.add(table);
	}
	
	private static void addSection4(Chapter chapter, Slr slrInstance, List<SearchHelper> searchs) throws DocumentException
	{
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		Paragraph sectionTitle = new Paragraph("Studies Searches", headerFont);
		sectionTitle.setSpacingBefore(10);
		Section section = chapter.addSection(sectionTitle);
		section.setIndentationLeft(10);
		
		Chunk chunk01 = new Chunk("Total", underlineFont);
		Chunk chunk02 = new Chunk(": " + searchs.size(), normalFont);
		Paragraph p = new Paragraph();
		p.add(chunk01); p.add(chunk02);
		p.setIndentationLeft(10);
		p.setSpacingBefore(10);
		section.add(p);
		
		PdfPTable table = new PdfPTable(6);
		table.setWidthPercentage(100); //Width 100%
        table.setSpacingBefore(10f); //Space before table
        table.setSpacingAfter(10f); //Space after table
        
        //Set Column widths
        float[] columnWidths = {0.5f, 1.5f, 1.2f, 1f, 1f, 1f};
        table.setWidths(columnWidths);
        
        PdfPCell cell = getHeaderCell("#");
        table.addCell(cell);
        
        cell = getHeaderCell("Source");
        table.addCell(cell);
        
        cell = getHeaderCell("Search Terms & Scope");
        table.addCell(cell);
        
        cell = getHeaderCell("Results");
        table.addCell(cell);
        
        cell = getHeaderCell("Primary Studies");
        table.addCell(cell);
        
        cell = getHeaderCell("Date");
        table.addCell(cell);
                
        int cont = 1;
        for(SearchHelper search : searchs)
        {
        	cell = getCell(Integer.toString(cont));
            table.addCell(cell);
        	
            cell = getCell(search.getEngines());
            table.addCell(cell);
            
        	cell = getCell(search.getTerms());
            table.addCell(cell);
            
            cell = getCell(Integer.toString(search.getResults()));
            table.addCell(cell);
            
            cell = getCell(Integer.toString(search.getPrimaryStudies()));
            table.addCell(cell);

            cell = getCell(df.format(search.getDate()));
            table.addCell(cell);

            cont++;
        }
        
        section.add(table);
	}
	
	private static void addSection5(Chapter chapter, Slr slrInstance, List<CriterionStudyHelper> criterions) throws DocumentException
	{
		Paragraph sectionTitle = new Paragraph("Study Selection", headerFont);
		sectionTitle.setSpacingBefore(10);
		Section section = chapter.addSection(sectionTitle);
		section.setIndentationLeft(10);
		
		Chunk chunk01 = new Chunk("Total", underlineFont);
		Chunk chunk02 = new Chunk(": " + criterions.size(), normalFont);
		Paragraph p = new Paragraph();
		p.add(chunk01); p.add(chunk02);
		p.setIndentationLeft(10);
		p.setSpacingBefore(10);
		section.add(p);
		
		PdfPTable table = new PdfPTable(5);
		table.setWidthPercentage(100); //Width 100%
        table.setSpacingBefore(10f); //Space before table
        table.setSpacingAfter(10f); //Space after table
        
        //Set Column widths
        float[] columnWidths = {0.5f, 1f, 1.5f, 1f, 1f};
        table.setWidths(columnWidths);
        
        PdfPCell cell = getHeaderCell("#");
        table.addCell(cell);
        
        cell = getHeaderCell("Criterion");
        table.addCell(cell);
        
        cell = getHeaderCell("Description");
        table.addCell(cell);
        
        cell = getHeaderCell("Studies");
        table.addCell(cell);
        
        cell = getHeaderCell("Frecuency");
        table.addCell(cell);
                
        int cont = 1;
        for(CriterionStudyHelper criterion : criterions)
        {
        	cell = getCell(Integer.toString(cont));
            table.addCell(cell);
        	
            cell = getCell(criterion.getName());
            table.addCell(cell);
            
        	cell = getCell(criterion.getDescription());
            table.addCell(cell);
            
            cell = getCell(Integer.toString(criterion.getStudies()));
            table.addCell(cell);
            
            cell = getCell(Double.toString(criterion.getFrecuency()));
            table.addCell(cell);

            cont++;
        }
        
        section.add(table);
	}
	
	private static void addSection6(Chapter chapter, List<PrimaryStudyHelper> primaryStudies) {
		
		Paragraph sectionTitle = new Paragraph("Primary Studies", headerFont);
		sectionTitle.setSpacingBefore(10);
		Section section = chapter.addSection(sectionTitle);
		section.setIndentationLeft(10);
		
		Chunk chunk01 = new Chunk("Total", underlineFont);
		Chunk chunk02 = new Chunk(": " + primaryStudies.size(), normalFont);
		Paragraph p = new Paragraph();
		p.add(chunk01); p.add(chunk02);
		p.setIndentationLeft(10);
		p.setSpacingBefore(10);
		section.add(p);
		
		int totalCol = 6 + primaryStudies.get(0).getSpecAttributes().size();
		
		PdfPTable table = new PdfPTable(totalCol);
		table.setWidthPercentage(100); //Width 100%
        table.setSpacingBefore(10f); //Space before table
        table.setSpacingAfter(10f); //Space after table
        
        PdfPCell cell = getHeaderCell("#");
        table.addCell(cell);
        
        cell = getHeaderCell("Title");
        table.addCell(cell);
        
        cell = getHeaderCell("Type");
        table.addCell(cell);
        
        cell = getHeaderCell("Year");
        table.addCell(cell);
        
        cell = getHeaderCell("Publisher");
        table.addCell(cell);
        
        cell = getHeaderCell("Citation Key");
        table.addCell(cell);
                
        int cont = 1;
    	Iterator it = null;
    	String key = "";
        for(PrimaryStudyHelper study : primaryStudies)
        {
        	// Insertamos el resto de cabeceras
        	if(cont == 1)
        	{
        		it = study.getSpecAttributes().keySet().iterator();
        		while(it.hasNext())
        		{
        			key = (String)it.next();
        			cell = getHeaderCell(key);
        	        table.addCell(cell);
        		}
        	}
        	
        	//#
        	cell = getCell(Integer.toString(cont));
            table.addCell(cell);
        	
            // Title
            cell = getCell(study.getTitle());
            table.addCell(cell);
            
            // Type
        	cell = getCell(study.getType());
            table.addCell(cell);
            
            // Year
            cell = getCell(Integer.toString(study.getYear()));
            table.addCell(cell);
            
            cell = getCell(study.getPublisher());
            table.addCell(cell);
            
            cell = getCell(study.getCitationKey());
            table.addCell(cell);
            
            it = study.getSpecAttributes().keySet().iterator();
            while(it.hasNext())
            {
            	key = (String) it.next();
            	
            	cell = getCell(study.getSpecAttributes().get(key));
                table.addCell(cell);
            }

            cont++;
        }
        
        section.add(table);
	}
	
	private static void addSection7(Chapter chapter, Map<String, AnnualTrend> annualTrends) throws DocumentException
	{
		Paragraph sectionTitle = new Paragraph("Annual Trends", headerFont);
		sectionTitle.setSpacingBefore(10);
		Section section = chapter.addSection(sectionTitle);
		section.setIndentationLeft(10);
		
		PdfPTable table = new PdfPTable(6);
		table.setWidthPercentage(100); //Width 100%
        table.setSpacingBefore(10f); //Space before table
        table.setSpacingAfter(10f); //Space after table
        
        //Set Column widths
        float[] columnWidths = {1f, 1f, 1f, 1f, 1f, 1f};
        table.setWidths(columnWidths);
        
        PdfPCell cell = getHeaderCell("Year");
        table.addCell(cell);
        
        cell = getHeaderCell("Books");
        table.addCell(cell);
        
        cell = getHeaderCell("Conference Proceedings");
        table.addCell(cell);
        
        cell = getHeaderCell("Generics");
        table.addCell(cell);
        
        cell = getHeaderCell("Journals");
        table.addCell(cell);
        
        cell = getHeaderCell("Others");
        table.addCell(cell);
        
        Iterator it = annualTrends.keySet().iterator();        
        while(it.hasNext())
        {
        	String year = (String) it.next();
        	
        	cell = getCell(year);
            table.addCell(cell);
            
            cell = getCell(annualTrends.get(year).getTotalBooks());
            table.addCell(cell);
            
            cell = getCell(annualTrends.get(year).getTotalConferences());
            table.addCell(cell);
            
            cell = getCell(annualTrends.get(year).getTotalGenerics());
            table.addCell(cell);
            
            cell = getCell(annualTrends.get(year).getTotalJournals());
            table.addCell(cell);
            
            cell = getCell(annualTrends.get(year).getTotalOthers());
            table.addCell(cell);
        }
        
        section.add(table);
	}
	
	private static PdfPCell getCell(String value)
	{
		PdfPCell cell = new PdfPCell(new Paragraph(value, tableFont));
        cell.setPaddingLeft(10);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        
        return cell;
	}
	
	private static PdfPCell getCell(int value)
	{
		return getCell(Integer.toString(value));		
	}
	
	private static PdfPCell getCell(double value)
	{
		return getCell(Double.toString(value));		
	}
	
	private static PdfPCell getHeaderCell(String value)
	{
		PdfPCell cell = new PdfPCell(new Paragraph(value));
        cell.setPaddingLeft(10);
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
        
        return cell;
	}
}
