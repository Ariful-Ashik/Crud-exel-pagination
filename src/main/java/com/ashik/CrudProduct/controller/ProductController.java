package com.ashik.CrudProduct.controller;


import com.ashik.CrudProduct.model.Product;
import com.ashik.CrudProduct.repository.ProductRepository;
import com.ashik.CrudProduct.service.ProductService;
import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping
    public List<Product> getAll() {
        return productService.findAlls();
    }

    @GetMapping("/{id}")
    public Product getById(@PathVariable Long id) {
        return productService.findById(id);
    }

    @PostMapping
    public Product create(@RequestBody Product product) {
        return productService.save(product);
    }

    @PutMapping("/{id}")
    public Product update(@PathVariable Long id, @RequestBody Product product) {
        product.setId(id);
        return productService.save(product);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        productService.delete(id);
    }


    @GetMapping("/export")
    public void exportToExcel(@RequestParam(required = false) String keyword,
                              @RequestParam(required = false) String status,
                              @RequestParam(required = false) String headers,
                              HttpServletResponse response) throws IOException {

        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setHeader("Content-Disposition", "attachment; filename=products.xlsx");

        List<Product> products = productService
                .searchProducts(keyword, status, Pageable.unpaged())
                .getContent();

        // Parse headers from query param
        List<String> headerList = (headers != null && !headers.isBlank())
                ? Arrays.stream(headers.split(","))
                .map(String::trim)
                .toList()
                : getDefaultHeaders(); // fallback if headers not sent

        exportExcel(products, headerList, response.getOutputStream());
    }
    private void exportExcel(List<Product> products, List<String> headers, OutputStream outputStream) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Products");

        // Create the header row
        Row headerRow = sheet.createRow(0);
        for (int i = 0; i < headers.size(); i++) {
            headerRow.createCell(i).setCellValue(headers.get(i));
        }

        int rowIdx = 1;
        for (Product product : products) {
            Row row = sheet.createRow(rowIdx++);
            int colIdx = 0;

            for (String header : headers) {
                String fieldName = convertHeaderToFieldName(header); // convert "Name" → "name"
                Object fieldValue = getFieldValueByName(product, fieldName);
                row.createCell(colIdx++).setCellValue(fieldValue != null ? fieldValue.toString() : "");
            }
        }

        workbook.write(outputStream);
        workbook.close();
    }
    private String convertHeaderToFieldName(String header) {
        // Simple conversion: "Product Name" → "productName", "ID" → "id"
        return header.trim()
                .replaceAll("[^a-zA-Z0-9]", "") // remove non-alphanumeric
                .replaceAll("(.)(\\p{Upper})", "$1$2") // keep camel case
                .toLowerCase();
    }

    private Object getFieldValueByName(Product product, String fieldName) {
        try {
            Field field = Product.class.getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(product);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return null; // or log warning
        }
    }
    private List<String> getDefaultHeaders() {
        return List.of("ID", "Na", "Pr", "Quan", "Stas");
    }
}

//    @GetMapping("/export")
//    public void exportToExcel(@RequestParam(required = false) String keyword,
//                              @RequestParam(required = false) String status,
//                              @RequestParam(required = false) Integer headers,
//                              HttpServletResponse response) throws IOException {
//
//        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
//        String headerKey = "Content-Disposition";
//        String headerValue = "attachment; filename=products.xlsx";
//        response.setHeader(headerKey, headerValue);
//
//        List<Product> products = productService.searchProducts(keyword, status, Pageable.unpaged()).getContent();
//        exportExcel(products, response.getOutputStream());
//    }
//
//    private void exportExcel(List<Product> products, OutputStream outputStream) throws IOException {
//        Workbook workbook = new XSSFWorkbook();
//        Sheet sheet = workbook.createSheet("Products");
//
//        Row headerRow = sheet.createRow(0);
//        String[] headers = {"ID", "Name", "Price", "Quantity", "Status"};
//
//        for (int i = 0; i < headers.length; i++) {
//            headerRow.createCell(i).setCellValue(headers[i]);
//        }
//
//        int rowIdx = 1;
//        for (Product p : products) {
//            Row row = sheet.createRow(rowIdx++);
//            row.createCell(0).setCellValue(p.getId());
//            row.createCell(1).setCellValue(p.getName());
//            row.createCell(2).setCellValue(p.getPrice());
//            row.createCell(3).setCellValue(p.getQuantity());
//            row.createCell(4).setCellValue(p.getStatus().toString());
//        }
//        workbook.write(outputStream);
//        workbook.close();
//    }
//}




