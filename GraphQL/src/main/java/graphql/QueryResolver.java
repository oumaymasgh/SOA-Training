package graphql;

// Import the business logic class responsible for managing "UniteEnseignement" data
import business.UniteEnseignementBusiness;

// Import the business logic class responsible for managing "Module" data
import business.ModuleBusiness;

// Import the GraphQL library class used to define the root of GraphQL queries
import com.coxautodev.graphql.tools.GraphQLRootResolver;

// Import the entity classes
import entities.UniteEnseignement;
import entities.Module;

import java.util.List;

/**
 * This class acts as the root resolver for GraphQL queries.
 * It tells GraphQL how to fetch data when a query is executed.
 *
 * It handles queries related to "UniteEnseignement" and "Module".
 */
public class QueryResolver implements GraphQLRootResolver {

    // Attribute used to call the business logic layer (service) for UE operations
    public UniteEnseignementBusiness helper;

    // Attribute used to call the business logic layer (service) for Module operations
    public ModuleBusiness moduleHelper;

    /**
     * Constructor of the resolver.
     * It initializes the business helpers that provide methods to retrieve data.
     */
    public QueryResolver(){
        helper = new UniteEnseignementBusiness();
        moduleHelper = new ModuleBusiness();
    }

    /**
     * GraphQL Query: allUEs
     *
     * This method will be automatically exposed to the GraphQL schema as a query named "allUEs".
     *
     * When a client sends the GraphQL query:
     *
     *    query {
     *        allUEs {
     *            code
     *            domaine
     *        }
     *    }
     *
     * GraphQL will call this method to return a list of "UniteEnseignement" objects.
     *
     * @return List of all Teaching Units (Unité d’Enseignement)
     */
    public List<UniteEnseignement> allUEs(){
        // Delegates the request to the business layer method getListeUE()
        // which fetches all UE entities (e.g., from a database or a static list)
        return helper.getListeUE();
    }

    /**
     * GraphQL Query: allModules
     *
     * When a client sends the GraphQL query:
     *
     *    query {
     *        allModules {
     *            matricule
     *            nom
     *            coefficient
     *            volumeHoraire
     *            type
     *        }
     *    }
     *
     * GraphQL will call this method to return a list of "Module" objects.
     *
     * @return List of all Modules
     */
    public List<Module> allModules(){
        // Delegates the request to the business layer
        // NOTE: replace getListeModules() with the real method name in your ModuleBusiness class
        return moduleHelper.getAllModules();
    }
}