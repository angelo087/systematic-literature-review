package es.uca.pfc

import grails.transaction.Transactional

import java.io.FileOutputStream;
import java.text.DateFormat
import java.text.SimpleDateFormat

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Color
import org.apache.poi.ss.usermodel.Comment
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormat
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Sheet
import org.apache.poi.ss.usermodel.Workbook
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFFont
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.openxml4j.opc.OPCPackage
import org.hibernate.internal.CriteriaImpl.CriterionEntry;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chapter
import com.itextpdf.text.Chunk
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Section
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import es.uca.pdf.AnnualTrend
import es.uca.pdf.CriterionStudyHelper;
import es.uca.pdf.ExportPdf;
import es.uca.pdf.PrimaryStudyHelper
import es.uca.pdf.SearchHelper

@Transactional
class ExportService {
	
	def INDEX_SHEET_INDEX = 0;
	def INDEX_SHEET_RESEARCH_QUESTION = 1;
	def INDEX_SHEET_SPECIFIC_ATTRIBUTE = 2;
	def INDEX_SHEET_STUDIES_SEARCHES = 3;
	def INDEX_SHEET_STUDY_SELECTION = 4;
	def INDEX_SHEET_PRIMARY_STUDIES = 5;
	def INDEX_SHEET_ANNUAL_TRENDS = 6;
	
    def serviceMethod() {

    }
	
	File exportToExcel(Slr slrInstance, String fileTemplate, String folderTmp)
	{
		Criterion criterionIncluded = Criterion.findBySlrAndNameLike(slrInstance,"included")
		List<Reference> referencesIncluded = new ArrayList<Reference>()
		for(Search search : slrInstance.searchs)
		{
			for(Reference reference : search.references)
			{
				if (reference.criterion.name.equals(criterionIncluded.name))
				{
					referencesIncluded.add(reference)
				}
			}
		}
		
		Workbook wb = new XSSFWorkbook( OPCPackage.open(fileTemplate) );
		
		CellStyle cellStyleBorder = wb.createCellStyle();
		cellStyleBorder.setBorderBottom(XSSFCellStyle.BORDER_THIN)
		cellStyleBorder.setBorderTop(XSSFCellStyle.BORDER_THIN)
		cellStyleBorder.setBorderRight(XSSFCellStyle.BORDER_THIN)
		cellStyleBorder.setBorderLeft(XSSFCellStyle.BORDER_THIN)
		cellStyleBorder.setWrapText(true)

		// Rellenamos el Excel
		wb = fillIndexExcel(wb, slrInstance)
		wb = fillResearchQuestionExcel(wb, slrInstance, cellStyleBorder)
		wb = fillSpecificAttributeExcel(wb, slrInstance, cellStyleBorder)
		wb = fillStudiesSearchesExcel(wb, slrInstance, criterionIncluded, cellStyleBorder)
		wb = fillStudySelectionExcel(wb, slrInstance, cellStyleBorder)
		wb = fillPrimaryStudiesExcel(wb, slrInstance, referencesIncluded, cellStyleBorder)
		wb = fillAnnualTrends(wb, slrInstance, referencesIncluded, cellStyleBorder)
		
		wb.setActiveSheet(INDEX_SHEET_INDEX)
		
		DateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String fileExcel = folderTmp + slrInstance.title.toString().trim().replaceAll(" ", "_") + "_" + df.format(new Date())+".xlsx";
		File file = new File(fileExcel)
		FileOutputStream fileOut = new FileOutputStream(file);
		wb.write(fileOut);
		fileOut.close();
		
		return file;
	}
	
	private Workbook fillIndexExcel(Workbook wb, Slr slrInstance)
	{
		Sheet sheet = wb.getSheetAt(INDEX_SHEET_INDEX);
		
		sheet.getRow(2).getCell(4).setCellValue("SLR: " + slrInstance.title);
		sheet.getRow(5).getCell(4).setCellValue(slrInstance.userProfile.display_name);
		
		return wb;
	}
	
	private Workbook fillResearchQuestionExcel(Workbook wb, Slr slrInstance, CellStyle cellStyleBorder)
	{
		Sheet sheet = wb.getSheetAt(INDEX_SHEET_RESEARCH_QUESTION);
		
		int fila = 1;
		for(ResearchQuestion question : slrInstance.questions)
		{
			if(null == sheet.getRow(fila))
			{
				sheet.createRow(fila)
			}
			
			def cell = sheet.getRow(fila).createCell(0)
			cell.setCellValue(fila);
			cell.setCellStyle(cellStyleBorder)
			cell = sheet.getRow(fila).createCell(1)
			cell.setCellValue(question.enunciado.toString())
			cell.setCellStyle(cellStyleBorder)
			
			fila++;
		}
		
		// Autosize colums
		sheet.autoSizeColumn(1)
		
		//Auto filters
		sheet.setAutoFilter(org.apache.poi.ss.util.CellRangeAddress.valueOf("A1:B1"))
		
		return wb;
	}
	
	private Workbook fillSpecificAttributeExcel(Workbook wb, Slr slrInstance, CellStyle cellStyleBorder)
	{
		Sheet sheet = wb.getSheetAt(INDEX_SHEET_SPECIFIC_ATTRIBUTE);
		
		int fila = 1;
		for(SpecificAttribute attribute : slrInstance.specAttributes)
		{
			if(null == sheet.getRow(fila))
			{
				sheet.createRow(fila)
			}
			
			def cell = sheet.getRow(fila).createCell(0)
			cell.setCellValue(fila)
			cell.setCellStyle(cellStyleBorder)
			cell = sheet.getRow(fila).createCell(1)
			cell.setCellValue(attribute.name)
			cell.setCellStyle(cellStyleBorder)
			cell = sheet.getRow(fila).createCell(2)
			cell.setCellValue(attribute.tipo)
			cell.setCellStyle(cellStyleBorder)
			
			cell = sheet.getRow(fila).createCell(3)
			if(attribute.tipo.equals("list"))
			{
				SpecificAttribute mulAttribute = (SpecificAttributeMultipleValue) attribute
				cell.setCellValue(mulAttribute.options.toString())
			}
			cell.setCellStyle(cellStyleBorder)
			fila++;
		}
		
		sheet.autoSizeColumn(1)
		sheet.autoSizeColumn(2)
		sheet.autoSizeColumn(3)
		
		sheet.setAutoFilter(org.apache.poi.ss.util.CellRangeAddress.valueOf("A1:D1"))
		
		return wb;
	}
	
	private Workbook fillStudiesSearchesExcel(Workbook wb, Slr slrInstance, Criterion criterionIncluded, CellStyle cellStyleBorder)
	{
		Sheet sheet = wb.getSheetAt(INDEX_SHEET_STUDIES_SEARCHES);
		CellStyle cellStyleDate = wb.createCellStyle();
		CreationHelper createHelper = wb.getCreationHelper();
		
		cellStyleDate.setDataFormat(createHelper.createDataFormat().getFormat("dd/MM/yyyy hh:mm"));
		cellStyleDate.setBorderBottom(XSSFCellStyle.BORDER_THIN)
		cellStyleDate.setBorderTop(XSSFCellStyle.BORDER_THIN)
		cellStyleDate.setBorderLeft(XSSFCellStyle.BORDER_THIN)
		cellStyleDate.setBorderRight(XSSFCellStyle.BORDER_THIN)
		
		int fila = 1;
		int pos = 0;
		for(Search search : slrInstance.searchs)
		{
			if (null == sheet.getRow(fila))
			{
				sheet.createRow(fila)
			}
			
			def cell = sheet.getRow(fila).createCell(0)
			String strEngines = "";
			for(EngineSearch engine : search.engines)
			{
				strEngines += engine.display_name
				if(pos != search.engines.size()-1)
				{
					strEngines += ", "
				}
				pos++
			}
			cell.setCellValue(strEngines)
			cell.setCellStyle(cellStyleBorder)
			cell = sheet.getRow(fila).createCell(1)
			
			String strTerminos = ""
			pos = 0
			for(SearchTermParam term : search.termParams)
			{
				strTerminos += term.operator.name + " '" + term.terminos + "' in " + term.component.name
				if(pos != search.termParams.size())
				{
					strTerminos += "\n"
				}
			}
			
			cell.setCellValue(strTerminos)
			cell.setCellStyle(cellStyleBorder)
			cell = sheet.getRow(fila).createCell(2)
			cell.setCellValue(search.references.size())
			cell.setCellStyle(cellStyleBorder)
			
			def listReferencesIncludes = Reference.findAllBySearchAndCriterion(search,criterionIncluded)
			cell = sheet.getRow(fila).createCell(3)
			cell.setCellValue(listReferencesIncludes.size())
			cell.setCellStyle(cellStyleBorder)
			
			cell = sheet.getRow(fila).createCell(4)
			cell.setCellStyle(cellStyleDate)
			cell.setCellValue(search.fecha)
			
			fila++;
		}
		
		//Auto filter
		sheet.setAutoFilter(org.apache.poi.ss.util.CellRangeAddress.valueOf("A1:F1"))
		
		return wb;
	}
	
	private Workbook fillStudySelectionExcel(Workbook wb, Slr slrInstance, CellStyle cellStyleBorder)
	{
		Sheet sheet = wb.getSheetAt(INDEX_SHEET_STUDY_SELECTION);
		CellStyle cellStyleNumeric = wb.createCellStyle();
		DataFormat format = wb.createDataFormat();
		cellStyleNumeric.setDataFormat(format.getFormat("0.00"));
		cellStyleNumeric.setBorderBottom(XSSFCellStyle.BORDER_THIN)
		cellStyleNumeric.setBorderTop(XSSFCellStyle.BORDER_THIN)
		cellStyleNumeric.setBorderLeft(XSSFCellStyle.BORDER_THIN)
		cellStyleNumeric.setBorderRight(XSSFCellStyle.BORDER_THIN)
		
		Map<String, Integer> mapCriterions = new HashMap<String, Integer>()
		
		for(Criterion criterion : slrInstance.criterions)
		{
			mapCriterions.put(criterion.name, 0)
		}
		
		int numReferences = 0;
		for(Search search : slrInstance.searchs)
		{
			for(Reference reference : search.references)
			{
				if (mapCriterions.containsKey(reference.criterion.name))
				{
					int value = mapCriterions.get(reference.criterion.name);
					mapCriterions.put(reference.criterion.name, value + 1)
				}
				numReferences++
			}
		}
		
		Iterator it = mapCriterions.keySet().iterator();
		int fila = 1;
		while(it.hasNext())
		{
			String key = it.next();
			
			if (null == sheet.getRow(fila))
			{
				sheet.createRow(fila)
			}
			
			def cell = sheet.getRow(fila).createCell(0)
			cell.setCellValue(key)
			cell.setCellStyle(cellStyleBorder)
			
			Criterion criterion = Criterion.findBySlrAndNameLike(slrInstance,key)
			cell = sheet.getRow(fila).createCell(1)
			cell.setCellValue(criterion.description)
			cell.setCellStyle(cellStyleBorder)
			
			cell = sheet.getRow(fila).createCell(2)
			cell.setCellValue(mapCriterions.get(key))
			cell.setCellStyle(cellStyleBorder)
			
			double porcentaje = (double) (mapCriterions.get(key) * 100) / numReferences;
			cell = sheet.getRow(fila).createCell(3)
			cell.setCellValue(porcentaje)
			cell.setCellStyle(cellStyleNumeric)
			
			fila++;
		}
		
		// Insertamos formula de sumas
		String strFormula = "SUM(C2:C" + fila +")";
		XSSFCell cell = sheet.getRow(1).createCell(5)
		cell.setCellType(XSSFCell.CELL_TYPE_FORMULA)
		cell.setCellFormula(strFormula)
		cell.setCellStyle(cellStyleBorder)
		
		strFormula = "SUM(D2:D" + fila +")";
		cell = sheet.getRow(1).createCell(6)
		cell.setCellType(XSSFCell.CELL_TYPE_FORMULA)
		cell.setCellFormula(strFormula)
		cell.setCellStyle(cellStyleBorder)
		
		// autosize colum
		sheet.autoSizeColumn(2)
		
		
		return wb;
	}
	
	private Workbook fillPrimaryStudiesExcel(Workbook wb, Slr slrInstance, List<Reference> referencesIncluded, CellStyle cellStyleBorder)
	{
		Sheet sheet = wb.getSheetAt(INDEX_SHEET_PRIMARY_STUDIES);
		
		XSSFFont fontBold = wb.createFont();
		fontBold.setBold(true);
		
		CellStyle cellStyleHeader = wb.createCellStyle();
		cellStyleHeader.setBorderBottom(XSSFCellStyle.BORDER_THIN)
		cellStyleHeader.setBorderTop(XSSFCellStyle.BORDER_THIN)
		cellStyleHeader.setBorderRight(XSSFCellStyle.BORDER_THIN)
		cellStyleHeader.setBorderLeft(XSSFCellStyle.BORDER_THIN)
		cellStyleHeader.setFont(fontBold)
		
		// Insertamos las cabeceras
		if(null == sheet.getRow(0))
		{
			sheet.createRow(0)
		}
		
		def cell
		
		cell = sheet.getRow(0).createCell(0)
		cell.setCellValue("#")
		cell.setCellStyle(cellStyleHeader)
		cell = sheet.getRow(0).createCell(1)
		cell.setCellValue("Title")
		cell.setCellStyle(cellStyleHeader)
		cell = sheet.getRow(0).createCell(2)
		cell.setCellValue("Type")
		cell.setCellStyle(cellStyleHeader)
		cell = sheet.getRow(0).createCell(3)
		cell.setCellValue("Year")
		cell.setCellStyle(cellStyleHeader)
		cell = sheet.getRow(0).createCell(4)
		cell.setCellValue("Publisher")
		cell.setCellStyle(cellStyleHeader)
		cell = sheet.getRow(0).createCell(5)
		cell.setCellValue("Citation Key")
		cell.setCellStyle(cellStyleHeader)
				
		int colStart = 6; //columna a partir de la cual estaran los atributos especificos
		int colEnd = colStart
		
		def attributes = slrInstance.specAttributes
		for(SpecificAttribute attribute : attributes)
		{
			cell = sheet.getRow(0).createCell(colStart)
			cell.setCellValue(attribute.name)
			cell.setCellStyle(cellStyleHeader)
			colStart++
		}
		
		int fila = 1;
		for(Reference reference : referencesIncluded)
		{
			if (null == sheet.getRow(fila))
			{
				sheet.createRow(fila)
			}
			
			cell = sheet.getRow(fila).createCell(0)
			cell.setCellValue(fila)
			cell.setCellStyle(cellStyleBorder)
			cell = sheet.getRow(fila).createCell(1)
			cell.setCellValue(reference.title)
			cell.setCellStyle(cellStyleBorder)
			cell = sheet.getRow(fila).createCell(2)
			cell.setCellValue(reference.type.nombre)
			cell.setCellStyle(cellStyleBorder)
			cell = sheet.getRow(fila).createCell(3)
			cell.setCellValue(reference.year)
			cell.setCellStyle(cellStyleBorder)
			cell = sheet.getRow(fila).createCell(4)
			cell.setCellValue(reference.publisher)
			cell.setCellStyle(cellStyleBorder)
			cell = sheet.getRow(fila).createCell(5)
			cell.setCellValue(reference.citation_key)
			cell.setCellStyle(cellStyleBorder)
			
			colStart = 6;
			colEnd = colStart
			for(SpecificAttribute attribute : attributes)
			{
				SpecificAttributeReference attributeReference = SpecificAttributeReference.findByAttributeAndReference(attribute,reference)
				cell = sheet.getRow(fila).createCell(colEnd)
				cell.setCellValue(attributeReference.value)
				cell.setCellStyle(cellStyleBorder)
				colEnd++
			}
			fila++;
		}
		
		// autosize columns
		sheet.autoSizeColumn(1)
		sheet.autoSizeColumn(2)
		sheet.autoSizeColumn(5)
		
		// autosize columns specific attributes
		for(int c = colStart; c < colEnd; c++)
		{
			sheet.autoSizeColumn(c)
		}
		
		//Auto filter
		//sheet.setAutoFilter(org.apache.poi.ss.util.CellRangeAddress.valueOf("1:1"))

		def firstCell = sheet.getRow(0).getCell(0)
		def lastCell  = sheet.getRow(0).getCell(colEnd-1)
		sheet.setAutoFilter(new org.apache.poi.ss.util.CellRangeAddress(firstCell.getRowIndex(), lastCell.getRowIndex(), firstCell.getColumnIndex(), lastCell.getColumnIndex()))
		
		return wb;
	}
	
	private Workbook fillAnnualTrends(Workbook wb, Slr slrInstance, List<Reference> referencesIncluded, CellStyle cellStyleBorder)
	{
		Sheet sheet = wb.getSheetAt(INDEX_SHEET_ANNUAL_TRENDS);		
		int minYear = 9999;
		int maxYear = 0;
		
		for(Reference reference : referencesIncluded)
		{
			int year = Integer.parseInt(reference.year)
			
			if (year <= minYear)
			{
				minYear = year;
			}
			
			if (year >= maxYear)
			{
				maxYear = year
			}
		}
		
		Map<String, Integer> mapBook = new HashMap<Integer, Integer>()
		Map<String, Integer> mapConference = new HashMap<Integer, Integer>()
		Map<String, Integer> mapGeneric = new HashMap<Integer, Integer>()
		Map<String, Integer> mapJournal = new HashMap<Integer, Integer>()
		Map<String, Integer> mapOther = new HashMap<Integer, Integer>()
		
		// Inicializar maps y columnas de a�os en el excel.
		int fila = 1;
		def cell
		for (int y = minYear; y<=maxYear; y++)
		{
			mapBook.put(y.toString(), 0);
			mapConference.put(y.toString(), 0);
			mapGeneric.put(y.toString(), 0);
			mapJournal.put(y.toString(), 0);
			mapOther.put(y.toString(), 0);
			
			if(null == sheet.getRow(fila))
			{
				sheet.createRow(fila)
			}
			
			cell = sheet.getRow(fila).createCell(0)
			cell.setCellValue(y)
			cell.setCellStyle(cellStyleBorder)
			fila++;
		}
		
		// Contabilizamos las referencias por sus tipos
		for(Reference reference : referencesIncluded)
		{
			if (reference.type.nombre.equals("Book"))
			{
				mapBook.put(reference.year, mapBook.get(reference.year)+1)
			}
			else if (reference.type.nombre.equals("Conference Proceedings"))
			{
				mapConference.put(reference.year, mapConference.get(reference.year)+1)
			}
			else if (reference.type.nombre.equals("Generic"))
			{
				mapGeneric.put(reference.year, mapGeneric.get(reference.year)+1)
			}
			else if (reference.type.nombre.equals("Journal"))
			{
				mapJournal.put(reference.year, mapJournal.get(reference.year)+1)
			}
			else
			{
				mapOther.put(reference.year, mapOther.get(reference.year)+1)
			}
		}
		
		// Insertamos Books en el excel
		Iterator it = mapBook.keySet().iterator();
		int columna = 1;
		fila = 1;
		while(it.hasNext())
		{
			String key = it.next();
			
			cell = sheet.getRow(fila).createCell(columna)
			cell.setCellValue(mapBook.get(key))
			cell.setCellStyle(cellStyleBorder)
			fila++;
		}
		
		// Insertamos Conferences en el excel
		it = mapConference.keySet().iterator();
		fila = 1;
		columna = 2;
		while(it.hasNext())
		{
			String key = it.next();
			
			cell = sheet.getRow(fila).createCell(columna)
			cell.setCellValue(mapConference.get(key))
			cell.setCellStyle(cellStyleBorder)
			fila++;
		}
		
		// Insertamos Generics en el excel
		it = mapGeneric.keySet().iterator();
		fila = 1;
		columna = 3;
		while(it.hasNext())
		{
			String key = it.next();
			
			cell = sheet.getRow(fila).createCell(columna)
			cell.setCellValue(mapGeneric.get(key))
			cell.setCellStyle(cellStyleBorder)
			fila++;
		}
		
		// Insertamos Journals en el excel
		it = mapJournal.keySet().iterator();
		fila = 1;
		columna = 4;
		while(it.hasNext())
		{
			String key = it.next();
			
			cell = sheet.getRow(fila).createCell(columna)
			cell.setCellValue(mapJournal.get(key))
			cell.setCellStyle(cellStyleBorder)
			fila++;
		}
		
		// Insertamos Others en el excel
		it = mapGeneric.keySet().iterator();
		fila = 1;
		columna = 5;
		while(it.hasNext())
		{
			String key = it.next();
			
			cell = sheet.getRow(fila).createCell(columna)
			cell.setCellValue(mapOther.get(key))
			cell.setCellStyle(cellStyleBorder)
			fila++;
		}
		
		//Auto filter
		sheet.setAutoFilter(org.apache.poi.ss.util.CellRangeAddress.valueOf("A1"))
		
		return wb;
	}
	
	File exportToPdf(Slr slrInstance, String folderTmp, String fileLogoUCA)
	{
		DateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String filePdf = folderTmp + slrInstance.title.toString().trim().replaceAll(" ", "_") + "_" + df.format(new Date())+".pdf";
		
		Criterion criterionIncluded = Criterion.findBySlrAndNameLike(slrInstance,"included")
		List<Reference> referencesIncluded = new ArrayList<Reference>()
		for(Search search : slrInstance.searchs)
		{
			for(Reference reference : search.references)
			{
				if (reference.criterion.name.equals(criterionIncluded.name))
				{
					referencesIncluded.add(reference)
				}
			}
		}
		
		List<ResearchQuestion> questions = new ArrayList<ResearchQuestion>()
		questions.addAll(slrInstance.questions)
		List<SpecificAttribute> attributes = new ArrayList<SpecificAttribute>()
		attributes.addAll(slrInstance.specAttributes)
		List<SearchHelper> searchs = getSearchsHelper(slrInstance, criterionIncluded)
		List<CriterionStudyHelper> criterionStudies = getCriterionStudiesHelper(slrInstance)
		List<PrimaryStudyHelper> primaryStudies = getPrimaryStudiesHelper(referencesIncluded)
		Map<String, AnnualTrend> annualTrends = getAnnualTrends(referencesIncluded);
				
		ExportPdf.createPdf(filePdf,fileLogoUCA,slrInstance, slrInstance.userProfile, questions, attributes, searchs, criterionStudies, primaryStudies, annualTrends)
		
		File file = new File(filePdf)
		
		return file;
	}
	
	List<CriterionStudyHelper> getCriterionStudiesHelper(Slr slrInstance)
	{
		List<CriterionStudyHelper> studies = new ArrayList<CriterionStudyHelper>()
		Map<String, Integer> mapCriterions = new HashMap<String, Integer>()
		
		for(Criterion criterion : slrInstance.criterions)
		{
			mapCriterions.put(criterion.name, 0)
		}
		
		int numReferences = 0;
		for(Search search : slrInstance.searchs)
		{
			for(Reference reference : search.references)
			{
				if (mapCriterions.containsKey(reference.criterion.name))
				{
					int value = mapCriterions.get(reference.criterion.name);
					mapCriterions.put(reference.criterion.name, value + 1)
				}
				numReferences++
			}
		}
		
		Iterator it = mapCriterions.keySet().iterator();
		int fila = 1;
		while(it.hasNext())
		{
			String key = it.next();
			
			CriterionStudyHelper critStud = new CriterionStudyHelper()
			
			critStud.setName(key);
			critStud.setStudies(mapCriterions.get(key));
			double porcentaje = (double) (mapCriterions.get(key) * 100) / numReferences;
			critStud.setFrecuency(porcentaje);
			
			def c = Criterion.findBySlrAndName(slrInstance, key)
			
			critStud.setDescription(c.description)
			
			studies.add(critStud)
		}
		
		return studies;
	}
	
	List<SearchHelper> getSearchsHelper(Slr slrInstance, Criterion criterionIncluded)
	{
		List<SearchHelper> searchsHelpers = new ArrayList<SearchHelper>();
		
		for(Search search : slrInstance.searchs)
		{
			String strEngines = "";
			int pos = 0;
			for(EngineSearch engine : search.engines)
			{
				strEngines += engine.display_name
				if(pos != search.engines.size())
				{
					strEngines += ", "
				}
				pos++
			}
			
			String strTerminos = "";
			for(SearchTermParam term : search.termParams)
			{
				strTerminos += term.operator.name + " '" + term.terminos + "' in " + term.component.name + "\n"
			}
			
			SearchHelper sHelp = new SearchHelper(strEngines, 
												  strTerminos,
												  search.references.size(), 
												  0, 
												  search.fecha)
			
			def listReferencesIncludes = Reference.findAllBySearchAndCriterion(search,criterionIncluded)
			
			sHelp.setPrimaryStudies(listReferencesIncludes.size())
			
			searchsHelpers.add(sHelp)
		}
		
		return searchsHelpers;
	}
	
	List<PrimaryStudyHelper> getPrimaryStudiesHelper(List<Reference> referencesIncluded)
	{
		List<PrimaryStudyHelper> studies = new ArrayList<PrimaryStudyHelper>();
		
		for(Reference reference : referencesIncluded)
		{
			PrimaryStudyHelper study = new PrimaryStudyHelper();
			
			study.setTitle(reference.title)
			study.setType(reference.type.nombre)
			study.setYear(Integer.parseInt(reference.year))
			study.setPublisher(reference.publisher)
			study.setCitationKey(reference.citation_key)
			
			Map<String, String> specAttributes = new HashMap<String, String>();
			
			for(SpecificAttributeReference attribute : reference.specificAttributes)
			{
				specAttributes.put(attribute.attribute.name, attribute.value)
			}
			study.setSpecAttributes(specAttributes)
			
			studies.add(study)
		}
		
		return studies;
	}
	
	Map<String, AnnualTrend> getAnnualTrends(List<Reference>referencesIncluded)
	{
		Map<String, AnnualTrend> trends = new TreeMap<String, AnnualTrend>()
		
		int minYear = 9999;
		int maxYear = 0;
		
		for(Reference reference : referencesIncluded)
		{
			int year = Integer.parseInt(reference.year)
			
			if (year <= minYear)
			{
				minYear = year;
			}
			
			if (year >= maxYear)
			{
				maxYear = year
			}
		}
		
		for(int y = minYear; y <= maxYear; y++)
		{
			trends.put(Integer.toString(y), new AnnualTrend())
		}
		
		// Contabilizamos las referencias por sus tipos
		for(Reference reference : referencesIncluded)
		{
			if (reference.type.nombre.equals("Book"))
			{
				trends.get(reference.year).setTotalBooks(trends.get(reference.year).getTotalBooks()+1)
			}
			else if (reference.type.nombre.equals("Conference Proceedings"))
			{
				trends.get(reference.year).setTotalConferences(trends.get(reference.year).getTotalConferences()+1)
			}
			else if (reference.type.nombre.equals("Generic"))
			{
				trends.get(reference.year).setTotalGenerics(trends.get(reference.year).getTotalGenerics()+1)
			}
			else if (reference.type.nombre.equals("Journal"))
			{
				trends.get(reference.year).setTotalJournals(trends.get(reference.year).getTotalJournals()+1)
			}
			else
			{
				trends.get(reference.year).setTotalOthers(trends.get(reference.year).getTotalOthers()+1)
			}
		}
		
		return trends;
	}
	
	File exportToBibTex(Slr slrInstance, String folderTmp)
	{
		Criterion criterionIncluded = Criterion.findBySlrAndNameLike(slrInstance,"included")
		List<Reference> referencesIncluded = new ArrayList<Reference>()
		for(Search search : slrInstance.searchs)
		{
			for(Reference reference : search.references)
			{
				if (reference.criterion.name.equals(criterionIncluded.name))
				{
					referencesIncluded.add(reference)
				}
			}
		}
		
		DateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String fileExcel = folderTmp + slrInstance.title.toString().trim().replaceAll(" ", "_") + "_" + df.format(new Date())+".bib";
		File file = new File(fileExcel)

		try
		{			
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			String content = "";
			for(Reference reference : referencesIncluded)
			{
				content += reference.bibtex + "\n"
			}
			bw.write(content)
			bw.close()
		}
		catch(Exception ex) {}
		
		return file;
	}
	
	File exportToBibTex(Reference referenceInstance)
	{
		DateFormat df = new SimpleDateFormat("yyyyMMdd_HHmmss");
		String fileExcel = "tmp/"+ referenceInstance.title.toString().trim().replaceAll(" ", "_") + "_" + df.format(new Date())+".bib";
		File file = new File(fileExcel)

		try
		{
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(referenceInstance.bibtex + "\n")
			bw.close()
		}
		catch(Exception ex) {}
		
		return file;
	}	
}
