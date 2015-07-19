/*
 * @(#DeveloperIdentityDao.java 07/16/2015
 * Copyright 2015 bitDubai, Inc. All rights reserved.
 * BITDUBAI/CONFIDENTIAL
 * */

package com.bitdubai.fermat_pip_plugin.layer.identity.developer.developer.bitdubai.version_1.structure;


// Packages and classes to import of jdk 1.7
import java.util.*;

// Packages and classes to import of fermat api
import com.bitdubai.fermat_api.DealsWithPluginIdentity;
import com.bitdubai.fermat_api.layer.all_definition.crypto.asymmetric.ECCKeyPair;
import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseFilterType;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTable;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableRecord;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DealsWithPluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantCreateDatabaseException;
import com.bitdubai.fermat_api.layer.osa_android.logger_system.LogLevel;
import com.bitdubai.fermat_api.layer.osa_android.logger_system.LogManager;
import com.bitdubai.fermat_pip_api.layer.pip_identity.developer.interfaces.DeveloperIdentity;
import com.bitdubai.fermat_api.layer.pip_user.device_user.interfaces_milestone2.DeviceUser;
import com.bitdubai.fermat_osa_addon.layer.android.logger.developer.bitdubai.version_1.structure.LoggerManager;
import com.bitdubai.fermat_pip_api.layer.pip_identity.developer.exceptions.CantGetUserDeveloperIdentitiesException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantLoadTableToMemoryException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.CantInsertRecordException;
import com.bitdubai.fermat_pip_api.layer.pip_identity.developer.exceptions.CantCreateNewDeveloperException;
import com.bitdubai.fermat_pip_plugin.layer.identity.developer.developer.bitdubai.version_1.exceptions.CantInitializeDeveloperIdentityDatabaseException;

// Packages and classes to import of apache commons.
import static org.apache.commons.lang3.StringUtils.isEmpty;


/**
 * The Class <code>com.bitdubai.fermat_pip_plugin.layer.identity.developer.developer.bitdubai.version_1.structure.DeveloperIdentityDao</code>
 * all methods implementation to access the data base<p/>
 * <p/>
 *
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 14/07/15.
 * Updated by Raul Pena   - (raul.pena@gmail.com)  on 16/07/15.
 *
 * @version 1.0
 * @since Java JDK 1.7
 */
public class DeveloperIdentityDao implements DealsWithPluginDatabaseSystem, DealsWithPluginIdentity {


    // Private instance fields declarations.
    // DealsWithPluginDatabaseSystem Interface member variables.
    private PluginDatabaseSystem pluginDatabaseSystem = null;

    //  Database factory
    private DeveloperIdentityDatabaseFactory databaseFactory = null;

    // Database object.
    private Database dataBase = null;

    private UUID     pluginId = null;



    // Private class fields declarations.
    // Logger object.
    private static final LogManager logger = new LoggerManager();

    // Blank target.
    private static final String _DEFAUL_STRING = "";



    // Public constructor declarations.
    /**
     *
     *  <p>Constructor without parameters.
     *
     *
     * */
    public DeveloperIdentityDao () {

        // Call to super class.
        super ();
    }

    /**
     *
     *  <p>Constructor with parameters.
     *
     *  @param pluginDatabaseSystem
     *  @param databaseFactory
     *  @param pluginId
     *
     * */
    public DeveloperIdentityDao (PluginDatabaseSystem pluginDatabaseSystem, DeveloperIdentityDatabaseFactory databaseFactory, UUID pluginId) {

        // Call to super class.
        super ();

        // Set internal values.
        this.pluginDatabaseSystem = pluginDatabaseSystem;
        this.databaseFactory = databaseFactory;
        this.pluginId = pluginId;
    }


    // Private instance methods declarations.
    /*
     *
     *  <p>Method that check if alias exists.
      *
      *  @param alias
      *  @return Boolean that indicate if the alias exists or not.
     * */
    private boolean aliasExists (String alias) throws CantCreateNewDeveloperException {


        // Setup method.
        DatabaseTable table; // Developer table.


        // Check the arguments.
        if (isEmpty (alias)) {

            // Cancel the process.
            logger.log (LogLevel.MINIMAL_LOGGING, "Alias is empty, arguments are null or empty.", _DEFAUL_STRING, _DEFAUL_STRING);
            return Boolean.FALSE;
        }

        if (this.dataBase == null) {

            // Cancel the process.
            logger.log (LogLevel.MINIMAL_LOGGING, "Cant check if alias exists or not, Database is closed o null.", _DEFAUL_STRING, _DEFAUL_STRING);
            throw new CantCreateNewDeveloperException ("Cant check if alias exists or not, Database is closed o null.", "Plugin Identity", "Cant check if alias exists or not, Database is closed o null.");
        }


        // Get developers identities list.
        try {

            logger.log (LogLevel.MINIMAL_LOGGING, "Checking if alias " + alias + " exists.", _DEFAUL_STRING, _DEFAUL_STRING);


            // 1) Get the table.
            logger.log(LogLevel.MINIMAL_LOGGING, "Getting " + DeveloperIdentityDatabaseConstants.DEVELOPER_TABLE_NAME + " table and record.", _DEFAUL_STRING, _DEFAUL_STRING);
            table = this.dataBase.getTable (DeveloperIdentityDatabaseConstants.DEVELOPER_TABLE_NAME);

            if (table == null) {

                // Table not found.
                logger.log (LogLevel.MINIMAL_LOGGING, "Cant check if alias exists, table not " + DeveloperIdentityDatabaseConstants.DEVELOPER_TABLE_NAME + " found.", _DEFAUL_STRING, _DEFAUL_STRING);
                throw new CantGetUserDeveloperIdentitiesException("Cant check if alias exists, table not \" + DeveloperIdentityDatabaseConstants.DEVELOPER_TABLE_NAME + \" found.", "Plugin Identity", "Cant check if alias exists, table not \" + DeveloperIdentityDatabaseConstants.DEVELOPER_TABLE_NAME + \" found.");
            }


            // 2) Find the developers.
            logger.log(LogLevel.MINIMAL_LOGGING, "Applying filter to " + DeveloperIdentityDatabaseConstants.DEVELOPER_TABLE_NAME + " table by developer alias key [" + alias.trim() + "].", _DEFAUL_STRING, _DEFAUL_STRING);
            table.setStringFilter(DeveloperIdentityDatabaseConstants.DEVELOPER_DEVELOPER_ALIAS_COLUMN_NAME, alias, DatabaseFilterType.EQUAL);
            table.loadToMemory();


            // 3) Get developers.
            logger.log(LogLevel.MINIMAL_LOGGING, "Developer alias found (" + table.getRecords().size() + ") by alias [" + alias + "].", _DEFAUL_STRING, _DEFAUL_STRING);
            return table.getRecords ().size () > 0;


        } catch (CantLoadTableToMemoryException em) {

            // Failure unknown.
            logger.log(LogLevel.MINIMAL_LOGGING, "Cant load " + DeveloperIdentityDatabaseConstants.DEVELOPER_TABLE_NAME + " table in memory.", _DEFAUL_STRING, _DEFAUL_STRING);
            throw new CantCreateNewDeveloperException (em.getMessage(), em, "Plugin Identity", "Cant load " + DeveloperIdentityDatabaseConstants.DEVELOPER_TABLE_NAME + " table in memory.");

        } catch (Exception e) {

            // Failure unknown.
            logger.log(LogLevel.MINIMAL_LOGGING, "Cant check if alias exists, unknown failure.", _DEFAUL_STRING, _DEFAUL_STRING);
            throw new CantCreateNewDeveloperException (e.getMessage(), e, "Plugin Identity", "Cant check if alias exists, unknown failure.");
        }
    }


    // Public instance methods declarations extends of com.bitdubai.fermat_api.layer.osa_android.database_system.DealsWithPluginDatabaseSystem
    /**
     * DealsWithPluginDatabaseSystem Interface implementation.
     */
    @Override
    public void setPluginDatabaseSystem (PluginDatabaseSystem pluginDatabaseSystem) {

        // Set internal values.
        this.pluginDatabaseSystem = pluginDatabaseSystem;
    }

    /**
     * DealsWithPluginIdentity Interface implementation.
     */
    @Override
    public void setPluginId (UUID pluginId) {

        // Set value.
        this.pluginId = pluginId;
    }



    // Public instance methods declarations.
    /**
     *
     *  <p>Method tha set the Database factory.
     * */
    public void setDeveloperIdentityDatabaseFactory (DeveloperIdentityDatabaseFactory databaseFactory) {

        // Set the value.
        this.databaseFactory = databaseFactory;
    }

    /**
     * This method open or creates the database i'll be working with.
     *
     * @param ownerId plugin id
     * @param databaseName database name
     * @throws CantInitializeDeveloperIdentityDatabaseException
     */
    public void initializeDatabase (UUID ownerId, String databaseName) throws CantInitializeDeveloperIdentityDatabaseException {


        // Check the arguments.
        if (ownerId == null || isEmpty (databaseName)) {

            // Cancel the process.
            logger.log(LogLevel.MINIMAL_LOGGING, "Cant create database, arguments are null or empty.", _DEFAUL_STRING, _DEFAUL_STRING);
            throw new CantInitializeDeveloperIdentityDatabaseException ("Cant create database, arguments are null or empty.", "Plugin Identity", "Cant create database, arguments are null or empty.");
        }


        // Create the database.
        try {

            logger.log(LogLevel.MINIMAL_LOGGING, "Initializing identity database system...", _DEFAUL_STRING, _DEFAUL_STRING);
            this.databaseFactory.setPluginDatabaseSystem(this.pluginDatabaseSystem);
            this.dataBase = this.databaseFactory.createDatabase (ownerId, isEmpty (databaseName) ? DeveloperIdentityDatabaseConstants.DEVELOPER_DB_NAME : databaseName);
            logger.log(LogLevel.MINIMAL_LOGGING, "Identity database initialized...", _DEFAUL_STRING, _DEFAUL_STRING);

        } catch (CantCreateDatabaseException e){

            logger.log(LogLevel.MINIMAL_LOGGING, "Cant create database", _DEFAUL_STRING, _DEFAUL_STRING);
            throw new CantInitializeDeveloperIdentityDatabaseException(e.getMessage(), e, "Plugin Identity", "Cant create database.");

        } catch (Exception e) {

            // Failure unknown.
            logger.log(LogLevel.MINIMAL_LOGGING, "Cant create database unknown failure.", _DEFAUL_STRING, _DEFAUL_STRING);
            throw new CantInitializeDeveloperIdentityDatabaseException (e.getMessage(), e, "Plugin Identity", "Cant create database, unknown failure.");
        }
    }

    /**
     * Method that create a new developer in the database.
     *
     * @param alias alias of developer
     * @param developerKeyPair new private and public key for the developer
     * @param deviceUser logged in device user
     * @return DeveloperIdentity
     * @throws CantCreateNewDeveloperException
     */
    public DeveloperIdentity createNewDeveloper (String alias, ECCKeyPair developerKeyPair, DeviceUser deviceUser) throws CantCreateNewDeveloperException {


        // Check the arguments.
        if (developerKeyPair == null || isEmpty (alias) || deviceUser == null) {

            // Cancel the process.
            logger.log (LogLevel.MINIMAL_LOGGING, "Cant create new developer, arguments are null or empty.", _DEFAUL_STRING, _DEFAUL_STRING);
            throw new CantCreateNewDeveloperException ("Cant create new developer, arguments are null or empty.", "Plugin Identity", "Cant create database, arguments are null or empty.");
        }


        if (this.dataBase == null) {

            // Cancel the process.
            logger.log (LogLevel.MINIMAL_LOGGING, "Cant check if alias exists or not, Database is closed o null.", _DEFAUL_STRING, _DEFAUL_STRING);
            throw new CantCreateNewDeveloperException ("Cant create new developer, Database is closed o null.", "Plugin Identity", "Cant create new developer, Database is closed o null.");
        }



            // Create the new developer.
            try {

                logger.log (LogLevel.MINIMAL_LOGGING, "Initializing developer record creation.", _DEFAUL_STRING, _DEFAUL_STRING);

                if (aliasExists (alias)) {

                    logger.log (LogLevel.MINIMAL_LOGGING, "Cant create new developer, alias exists.", _DEFAUL_STRING, _DEFAUL_STRING);
                    throw new CantCreateNewDeveloperException ("Cant create new developer, alias exists.", "Plugin Identity", "Cant create new developer, alias exists.");
                }

                logger.log (LogLevel.MINIMAL_LOGGING, "Getting " + DeveloperIdentityDatabaseConstants.DEVELOPER_TABLE_NAME + " table and record.", _DEFAUL_STRING, _DEFAUL_STRING);
                DatabaseTable        table = this.dataBase.getTable (DeveloperIdentityDatabaseConstants.DEVELOPER_TABLE_NAME);
                DatabaseTableRecord record = table.getEmptyRecord ();


                logger.log (LogLevel.MINIMAL_LOGGING, "Getting " + DeveloperIdentityDatabaseConstants.DEVELOPER_TABLE_NAME + " table and record.", _DEFAUL_STRING, _DEFAUL_STRING);
                record.setStringValue(DeveloperIdentityDatabaseConstants.DEVELOPER_DEVELOPER_PUBLIC_KEY_COLUMN_NAME, developerKeyPair.getPublicKey());
                record.setStringValue(DeveloperIdentityDatabaseConstants.DEVELOPER_DEVELOPER_PRIVATE_KEY_COLUMN_NAME, developerKeyPair.getPrivateKey());
                record.setStringValue(DeveloperIdentityDatabaseConstants.DEVELOPER_DEVICE_USER_PUBLIC_KEY_COLUMN_NAME, deviceUser.getPublicKey());
                record.setStringValue(DeveloperIdentityDatabaseConstants.DEVELOPER_DEVELOPER_ALIAS_COLUMN_NAME, deviceUser.getAlias());


                logger.log(LogLevel.MINIMAL_LOGGING, "Inserting [Alias=" + deviceUser.getAlias() + ", PK=" + developerKeyPair.getPublicKey() + "] in " + DeveloperIdentityDatabaseConstants.DEVELOPER_TABLE_NAME + " table and record.", _DEFAUL_STRING, _DEFAUL_STRING);
                table.insertRecord(record);
                logger.log(LogLevel.MINIMAL_LOGGING, "New developer record created [" + developerKeyPair.getPublicKey() + "]", _DEFAUL_STRING, _DEFAUL_STRING);

            } catch (CantInsertRecordException e){

                // Cant insert record.
                logger.log (LogLevel.MINIMAL_LOGGING, "Cant create new developer, insert database problems.", _DEFAUL_STRING, _DEFAUL_STRING);
                throw new CantCreateNewDeveloperException (e.getMessage(), e, "Plugin Identity", "Cant create new developer, insert database problems.");

            } catch (Exception e) {

                // Failure unknown.
                logger.log(LogLevel.MINIMAL_LOGGING, "Cant create new developer, unknown failure.", _DEFAUL_STRING, _DEFAUL_STRING);
                throw new CantCreateNewDeveloperException (e.getMessage(), e, "Plugin Identity", "Cant create new developer, unknown failure.");
            }


        // Return the new developer.
        return new DeveloperIdentityRecord (alias, developerKeyPair.getPublicKey ());
    }

    /**
     * Method that list the developers related to the parametrized device user.
     *
     * @param deviceUser device user
     * @throws CantGetUserDeveloperIdentitiesException
     */
    public List<DeveloperIdentity> getDevelopersFromCurrentDeviceUser (DeviceUser deviceUser) throws CantGetUserDeveloperIdentitiesException {


        // Setup method.
        List<DeveloperIdentity> list = new ArrayList<DeveloperIdentity> (); // Developer list.
        DatabaseTable table; // Developer table.


        // Check the arguments.
        if (deviceUser == null) {

            // Cancel the process.
            logger.log (LogLevel.MINIMAL_LOGGING, "Cant get developers from current device, arguments are null or empty.", _DEFAUL_STRING, _DEFAUL_STRING);
            throw new CantGetUserDeveloperIdentitiesException ("Cant get developers from current device, arguments are null or empty.", "Plugin Identity", "Cant get developers from current device, arguments are null or empty.");
        }

        if (this.dataBase == null) {

            // Cancel the process.
            logger.log (LogLevel.MINIMAL_LOGGING, "Cant check if alias exists or not, Database is closed o null.", _DEFAUL_STRING, _DEFAUL_STRING);
            throw new CantGetUserDeveloperIdentitiesException ("Cant get developers from current device, Database is closed o null.", "Plugin Identity", "Cant get developers from current device, Database is closed o null.");
        }


            // Get developers identities list.
            try {

                logger.log (LogLevel.MINIMAL_LOGGING, "Getting developer list.", _DEFAUL_STRING, _DEFAUL_STRING);

                // 1) Get the table.
                logger.log (LogLevel.MINIMAL_LOGGING, "Getting " + DeveloperIdentityDatabaseConstants.DEVELOPER_TABLE_NAME + " table and record.", _DEFAUL_STRING, _DEFAUL_STRING);
                table = this.dataBase.getTable (DeveloperIdentityDatabaseConstants.DEVELOPER_TABLE_NAME);

                if (table == null) {

                    // Table not found.
                    logger.log (LogLevel.MINIMAL_LOGGING, "Cant get developer identity list, table not " + DeveloperIdentityDatabaseConstants.DEVELOPER_TABLE_NAME + " found.", _DEFAUL_STRING, _DEFAUL_STRING);
                    throw new CantGetUserDeveloperIdentitiesException ("Cant get developer identity list, table not \" + DeveloperIdentityDatabaseConstants.DEVELOPER_TABLE_NAME + \" found.", "Plugin Identity", "Cant get developer identity list, table not \" + DeveloperIdentityDatabaseConstants.DEVELOPER_TABLE_NAME + \" found.");
                }


                // 2) Find the developers.
                logger.log (LogLevel.MINIMAL_LOGGING, "Applying filter to " + DeveloperIdentityDatabaseConstants.DEVELOPER_TABLE_NAME + " table by developer public key [" + deviceUser.getPublicKey() + "].", _DEFAUL_STRING, _DEFAUL_STRING);
                table.setStringFilter(DeveloperIdentityDatabaseConstants.DEVELOPER_DEVELOPER_PUBLIC_KEY_COLUMN_NAME, deviceUser.getPublicKey(), DatabaseFilterType.EQUAL);
                table.loadToMemory();


                // 3) Get developers.
                logger.log (LogLevel.MINIMAL_LOGGING, "Developer identity found (" + table.getRecords ().size () + ") by public key [" + deviceUser.getPublicKey () + "].", _DEFAUL_STRING, _DEFAUL_STRING);
                for (DatabaseTableRecord record : table.getRecords ()) {

                    // Add records to list.
                    list.add(new DeveloperIdentityRecord (record.getStringValue(DeveloperIdentityDatabaseConstants.DEVELOPER_DEVELOPER_ALIAS_COLUMN_NAME),
                            record.getStringValue (DeveloperIdentityDatabaseConstants.DEVELOPER_DEVELOPER_PUBLIC_KEY_COLUMN_NAME)));
                }


            } catch (CantLoadTableToMemoryException em) {

                // Failure unknown.
                logger.log (LogLevel.MINIMAL_LOGGING, "Cant load " + DeveloperIdentityDatabaseConstants.DEVELOPER_TABLE_NAME + " table in memory.", _DEFAUL_STRING, _DEFAUL_STRING);
                throw new CantGetUserDeveloperIdentitiesException (em.getMessage(), em, "Plugin Identity", "Cant load " + DeveloperIdentityDatabaseConstants.DEVELOPER_TABLE_NAME + " table in memory.");

            } catch (Exception e) {

                // Failure unknown.
                logger.log (LogLevel.MINIMAL_LOGGING, "Cant get developer identity list, unknown failure.", _DEFAUL_STRING, _DEFAUL_STRING);
                throw new CantGetUserDeveloperIdentitiesException (e.getMessage(), e, "Plugin Identity", "Cant get developer identity list, unknown failure.");
            }


        // Return the list values.
        return list;
    }
}
