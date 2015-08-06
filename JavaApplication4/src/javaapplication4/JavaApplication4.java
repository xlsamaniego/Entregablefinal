/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javaapplication4;

//import static com.hp.hpl.jena.assembler.JA.Model;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.vocabulary.RDF;
import com.hp.hpl.jena.vocabulary.RDFS;
//import com.hp.hpl.jena.vocabulary.VCARD;
//import java.io.File;
import jxl.*;
import java.io.*;

/**
 *
 * @author Ximena Samaniego
 */
public class JavaApplication4 {

    /**
     * @param args the command line arguments
     */
    private void Archivo(String archivoEvento){

    //String proyectoURI = "http://utpl.edu.ec/sbc/proyectoTemperatura";
               
        String N_registro = "http://schema.org/Number";
        String titulo = "http://purl.org/dc/terms/title";
        String hora = "http://utpl.edu.ec/sbc/proyectoTemperatura/hora";
        String gradoTemperatura = "http://utpl.edu.ec/sbc/proyectoTemperatura/gradoTemperatura";
        String temperatura = "http://purl.oclc.org/NET/ssnx/meteo/aws#TemperatureSensor";     
        String ubicacion = "http://utpl.edu.ec/sbc/proyectoTemperatura/ubicacion";  
        String sectores = "http://utpl.edu.ec/sbc/proyectoTemperatura/sector";
        
        String URI_sectorClass = "http://schema.org/Place";
        String URI_temperaturaClass = "http://purl.oclc.org/NET/ssnx/meteo/aws#TemperatureSensor";
        String URI_Parque="http://utpl.edu.ec/sbc/proyectoTemperatura/Parques_Loja";

// create the resource
// and add the properties cascading style
         Property N_registroNameProperty = ResourceFactory.createProperty(N_registro);
         Property tituloNameProperty = ResourceFactory.createProperty(titulo);
         Property horaNameProperty = ResourceFactory.createProperty(hora);
         Property gradoTemperaturaNameProperty = ResourceFactory.createProperty(gradoTemperatura);
         Property temperaturaNameProperty = ResourceFactory.createProperty(temperatura);
         Property ubicacionNameProperty = ResourceFactory.createProperty(ubicacion);
         Property sectoresNameProperty = ResourceFactory.createProperty(sectores);
         
         Model model = ModelFactory.createDefaultModel();
         
                  
            try {
            Workbook archivo = Workbook.getWorkbook(new File(archivoEvento));
            System.out.println("**** Existen \t" + archivo.getNumberOfSheets()+" hojas en el Excel****");
            for (int sheetNo = 0; sheetNo < archivo.getNumberOfSheets(); sheetNo++) {
                Sheet hoja = archivo.getSheet(sheetNo);
                int numColumnas = hoja.getColumns();
                int numFilas = hoja.getRows();
                String data[][] = new String[numColumnas][numFilas];
                System.out.println("***** El Nombre de la hoja del Excel es \t" + archivo.getSheet(sheetNo).getName());
                for (int fila = 0; fila <2; fila++) { //////////// EN FILA < LE PONES EL VALOR DE LAS FILAS QUE QUIERAS QUE APAREZCAN 
                    for (int columna = 0; columna < numColumnas; columna++) { 
                        data[columna][fila] = hoja.getCell(columna, fila).getContents();
                    }
                    String nombre_parque="http://utpl.edu.ec/sbc/proyectoTemperatura/"+ data[5][fila];
                    String objeto="http://utpl.edu.ec/sbc/proyectoTemperatura/Captura"+ fila;
                    ////separador de las tripletas
              //      System.out.println("\n---------------------------------------------------------------------------");
               //     System.out.println("\n---------------------------------------------------------------------------");

                    // create an empty Model
                    
                    
                    Resource Sector
                            = model.createResource(URI_sectorClass)
                            .addProperty(RDF.type, model.createResource("http://www.w3.org/2000/01/rdf-schema#class"));
                    
                    Resource Nombres_Parques=model.createResource(nombre_parque)
                            .addProperty(sectoresNameProperty, "" + data[6][fila])
                            .addProperty(RDFS.comment,"Nombre del parque")
                            .addProperty(RDF.type, model.createResource(URI_Parque)
                                            .addProperty(RDFS.label, "Parques de Loja")
                                            .addProperty(RDFS.comment, "Clase que hace refrenecia a los Parques de la ciudad de Loja")
                                            .addProperty(RDF.type, Sector));
                    
                  
                    
                    Resource Object=model.createResource(objeto)
                            .addProperty(RDFS.label, "Temperaturs captadad en los Parques")
                            
                            .addProperty(N_registroNameProperty, "" + data[0][fila])                                  
                            .addProperty(tituloNameProperty, "" + data[1][fila])
                            .addProperty(horaNameProperty, "" + data[2][fila])
                            .addProperty(gradoTemperaturaNameProperty, "" + data[3][fila])
                            .addProperty(temperaturaNameProperty, "" + data[4][fila])
                            .addProperty(ubicacionNameProperty, Nombres_Parques )
                            ;
                    
                     Resource Temperatura
                            = model.createResource(URI_temperaturaClass)
                             .addProperty(RDF.type, Object)
                            .addProperty(RDFS.label, "Sensor de Temperatura")
                             .addProperty(RDFS.comment, "Clase que hace referencia a la temperatura capturada por el sennsor")
                            .addProperty(RDF.type, model.createResource("http://www.w3.org/2000/01/rdf-schema#class"))
                            ;

                }
                 model.write(System.out, "RDF/XML"); 
            }
        } catch (Exception ioe) {
            ioe.printStackTrace();
        }
    }
    public static void main(String arg[]) {
        JavaApplication4 excel = new JavaApplication4();
        excel.Archivo("proyecto.xls");
    }

}
