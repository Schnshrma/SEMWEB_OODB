import org.apache.jena.atlas.iterator.Iter;
import org.apache.jena.ontology.*;
import org.apache.jena.rdf.model.*;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.util.FileManager;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.apache.jena.vocabulary.RDFS;

import java.io.InputStream;
import java.util.*;

public class rdf_read {
    public static void main(String[] args) {
        // create an empty model
        OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        String ns = "http://www.w3.org/2002/07/owl#";
        // use the RDFDataMgr to find the input file
        InputStream in = RDFDataMgr.open("src/main/resources/Restaurant_Ontology.owl");
        if (in == null) {
            throw new IllegalArgumentException(
                    "File: sample.rdf +  not found");
        }

// read the RDF/XML file
        model.read(in, null);

        ExtendedIterator<OntClass> iterator = model.listClasses();
        List<String> classes = new ArrayList<String>();
        HashMap<String, List<String>> ClassSuperclassUris = new HashMap<String, List<String>>();
        HashMap<String, String> UrisToClassLabel = new HashMap<String, String>();
        List<List<String>> AllProps = new ArrayList<List<String>>();
        while (iterator.hasNext()) {
            OntClass ontClass = (OntClass) iterator.next();
            String uri = ontClass.getURI();
            String[] arrOfStr = uri.split("#", 2);
            System.out.println("\n\nClass is : " + arrOfStr[1]);
            classes.add(arrOfStr[1]);
            //           System.out.println("Class is : " + ontClass.toString());
//            ontClass.getLocalName();
//            System.out.println("Label is :"+ontClass.getLabel("en"));
            UrisToClassLabel.put(ontClass.toString(), ontClass.getLabel("en"));
            // classes.add(arrOfStr[1]);
//            if(ontClass.hasSubClass()){
//                System.out.println("SubClass is : " + ontClass.getSubClass());
//                OntClass subclass=ontClass.getSubClass();
////                System.out.println("Subclass Label is :"+ ontClass.getLabel("en"));
//            }
            if (ontClass.hasSuperClass()) {
//                System.out.println("SuperClassLabel is :"+ ontClass.getLabel("en"));
                System.out.println("SuperClass is : " + ontClass.getSuperClass());
                if (ClassSuperclassUris.containsKey(ontClass.toString())) {
                    List<String> temp = ClassSuperclassUris.get(ontClass.toString());
                    temp.add(ontClass.getSuperClass().toString());
                    ClassSuperclassUris.put(ontClass.getLabel("en"), temp);
                } else {
                    List<String> temp = new ArrayList<String>();
                    temp.add(ontClass.getSuperClass().toString());
                    System.out.println(ontClass.getLabel("en"));
                    ClassSuperclassUris.put(ontClass.getLabel("en"), temp);
                }

            }

            //Trying to get properties
            System.out.println("Properties->");
            ExtendedIterator<OntProperty> iterprop = ontClass.listDeclaredProperties();

            //ontClass.listAllOntProperties();

            while (iterprop.hasNext()) {
//                System.out.println(iterprop.next());
                OntProperty prop = (OntProperty) iterprop.next();
                String domain = "";
                domain = domain + prop.getDomain();
                String[] arrOfStr1 = domain.split("#", 2);
                domain = arrOfStr1[1];

                String range = "";
                range = range + prop.getRange();
                String[] arrOfStr2 = range.split("#", 2);
                range = arrOfStr2[1];

                List<String> temp = new ArrayList<String>();

                temp.add(domain);
                temp.add(prop.getLocalName());
                temp.add(range);
                AllProps.add(temp);
//                System.out.println(prop.getLabel("en")+" "+UrisToClassLabel.get(domain)+" "+UrisToClassLabel.get(range));
                System.out.println(domain + " " + prop.getLocalName() + " " + range);

            }
            //End of master while
        }

        for (int i = 0; i < classes.size(); i++) {
            System.out.println(classes.get(i));
        }


//======================================================================================================
        //Individulas
        Iterator indi = model.listIndividuals();
        while (indi.hasNext()) {
            Individual indiv = (Individual) indi.next();

            System.out.println(indiv.getLocalName());
        }


        StmtIterator iterator1 = model.listStatements();
        while (iterator1.hasNext()) {
            System.out.println("*************************************");
            Statement statement = iterator1.nextStatement();
            Resource subject = statement.getSubject();
            Property predicate = statement.getPredicate();
            RDFNode object = statement.getObject();
            System.out.println("Subject is: " + subject.getLocalName());
            System.out.println("Predicate is: " + predicate.getLocalName());
            System.out.println("Object is: " + object.toString());
           // System.out.println("Class is: " + statement.getClass().toString());
            if (object instanceof OntClass) {
              //  System.out.println("Object is: " + object.toString());
            }
        }
// write it to standard out
//        model.write(System.out);
    }
}





