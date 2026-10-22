package graphql;

// Import the business logic class responsible for managing "UniteEnseignement" data
import business.UniteEnseignementBusiness;

// Import the business logic class responsible for managing "Module" data
import business.ModuleBusiness;

// Import the GraphQL library class used to define the root of GraphQL mutations
import com.coxautodev.graphql.tools.GraphQLRootResolver;

// Import the entity classes
import entities.UniteEnseignement;
import entities.Module;
import entities.Module.TypeModule;
/**
 * This class defines all GraphQL mutations related to the "UniteEnseignement" and "Module" entities.
 * A mutation in GraphQL is used to perform write operations (create, update, delete)
 * as opposed to queries, which are for read operations.
 */
public class MutationResolver implements GraphQLRootResolver {

    // Attribute used to access the business logic (service layer) for teaching units
    public UniteEnseignementBusiness helper;

    // Attribute used to access the business logic (service layer) for modules
    public ModuleBusiness moduleHelper;

    /**
     * Constructor of the resolver.
     * It initializes the business helpers that manage the teaching units and modules.
     */
    public MutationResolver() {
        helper = new UniteEnseignementBusiness();
        moduleHelper = new ModuleBusiness();
    }

    /**
     * GraphQL Mutation: addUniteEnseignement
     *
     * This method allows clients to add a new "Unité d’Enseignement" to the system.
     *
     * When a GraphQL mutation like the following is executed:
     *
     * mutation {
     *     addUniteEnseignement(
     *         code: 101,
     *         domaine: "Informatique",
     *         responsable: "Dr. Smith",
     *         credits: 4,
     *         semestre: 2
     *     )
     * }
     *
     * GraphQL calls this Java method to create and save a new teaching unit.
     */
    public boolean addUniteEnseignement(int code, String domaine, String responsable, int credits, int semestre) {
        // Create a new UniteEnseignement object with the provided parameters
        UniteEnseignement ue = new UniteEnseignement(code, domaine, responsable, credits, semestre);

        // Call the business logic method to add the new unit
        // The helper handles the actual persistence or logic of adding the entity
        return helper.addUniteEnseignement(ue);
    }

    /**
     * GraphQL Mutation: addModule
     *
     * When a GraphQL mutation like the following is executed:
     *
     * mutation {
     *     addModule(
     *         matricule: "M101",
     *         libelle: "SOA",
     *         coef: 2,
     *         volumeHoraire: 21,
     *         codeUE: 1,
     *         type: TRANSVERSAL
     *     )
     * }
     *
     * GraphQL calls this Java method to create and save a new module.
     */
    public boolean addModule(String matricule, String libelle, int coef,
                             int volumeHoraire, int codeUE, TypeModule type) {
        // Find the teaching unit this module belongs to
        // NOTE: adjust getUEByCode to the real method name in UniteEnseignementBusiness
        UniteEnseignement ue = helper.getUEByCode(codeUE);

        // Create the module with the provided parameters
        // NOTE: the constructor parameter order must match your Module class
        Module m = new Module(matricule, libelle, coef, volumeHoraire, type, ue);

        // Delegate persistence/logic to the business layer
        return moduleHelper.addModule(m);
    }
    /**
     * GraphQL Mutation: updateModule
     *
     * mutation {
     *     updateModule(
     *         matricule: "M101",
     *         libelle: "Algorithmique",
     *         coef: 3,
     *         volumeHoraire: 21,
     *         codeUE: 1,
     *         type: PROFESSIONNEL
     *     ) {
     *         matricule
     *         nom
     *         volumeHoraire
     *         type
     *     }
     * }
     */
    public Module updateModule(String matricule, String libelle, int coef,
                               int volumeHoraire, int codeUE, TypeModule type) {
        // The new teaching unit must exist
        UniteEnseignement ue = helper.getUEByCode(codeUE);
        if (ue == null) {
            return null;
        }

        Module updated = new Module(matricule, libelle, coef, volumeHoraire, type, ue);

        // Returns the updated module, or null if no module has this matricule
        if (moduleHelper.updateModule(matricule, updated)) {
            return updated;
        }
        return null;
    }

    /**
     * GraphQL Mutation: deleteModule
     *
     * mutation {
     *     deleteModule(matricule: "M101")
     * }
     */
    public boolean deleteModule(String matricule) {
        return moduleHelper.deleteModule(matricule);
    }
}