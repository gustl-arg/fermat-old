package unit.com.bitdubai.fermat_pip_plugin.layer.network_service.subapp_resources.developer.bitdubai.version_1.SubAppResourcesInstallationNetworkServicePluginRoot;

import com.bitdubai.fermat_api.layer.all_definition.events.interfaces.FermatEventListener;
import com.bitdubai.fermat_api.layer.all_definition.github.GithubConnection;
import com.bitdubai.fermat_api.layer.all_definition.resources_structure.Skin;
import com.bitdubai.fermat_api.layer.all_definition.util.XMLParser;
import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseFactory;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTable;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableFactory;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableRecord;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTransaction;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.file_system.FileLifeSpan;
import com.bitdubai.fermat_api.layer.osa_android.file_system.FilePrivacy;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginBinaryFile;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginFileSystem;
import com.bitdubai.fermat_api.layer.osa_android.file_system.PluginTextFile;
import com.bitdubai.fermat_pip_api.layer.pip_network_service.subapp_resources.exceptions.CantInstallCompleteSubAppResourcesException;
import com.bitdubai.fermat_pip_api.layer.platform_service.error_manager.ErrorManager;
import com.bitdubai.fermat_pip_api.layer.platform_service.event_manager.enums.EventType;
import com.bitdubai.fermat_pip_api.layer.platform_service.event_manager.interfaces.EventManager;
import com.bitdubai.fermat_pip_plugin.layer.network_service.subapp_resources.developer.bitdubai.version_1.SubAppResourcesInstallationNetworkServicePluginRoot;
import com.bitdubai.fermat_pip_plugin.layer.network_service.subapp_resources.developer.bitdubai.version_1.database.SubAppResourcesInstallationNetworkServiceDAO;
import com.bitdubai.fermat_pip_plugin.layer.network_service.subapp_resources.developer.bitdubai.version_1.database.SubAppResourcesNetworkServiceDatabaseConstants;
import com.bitdubai.fermat_pip_plugin.layer.network_service.subapp_resources.developer.bitdubai.version_1.estructure.Repository;
import com.googlecode.catchexception.CatchException;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.UUID;

import static com.googlecode.catchexception.CatchException.catchException;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by francisco on 30/09/15.
 */
@RunWith(MockitoJUnitRunner.class)
public class InstallCompleteSubAppTest extends TestCase {

    /**
     * DealsWithErrors interface Mocked
     */
    @Mock
    ErrorManager errorManager;

    /**
     * UsesFileSystem Interface member variables.
     */
    @Mock
    PluginFileSystem pluginFileSystem;


    /**
     * DealWithEvents Iianterface member variables.
     */
    @Mock
    private FermatEventListener mockFermatEventListener;

    @Mock
    private EventManager mockEventManager;

    @Mock
    private Database mockDatabase;

    @Mock
    private PluginDatabaseSystem mockPluginDatabaseSystem;

    @Mock
    private PluginTextFile mockPluginTextFile;

    @Mock
    private Repository repository;

    @Mock
    private GithubConnection githubConnection;

    @Mock
    private XMLParser mockXMLParser;

    @Mock
    private PluginTextFile layoutFile;
    @Mock
    private PluginBinaryFile imageFile;

    @Mock
    private DatabaseTableFactory mockFactoryTable;

    @Mock
    private DatabaseFactory mockDatabaseFactory;

    @Mock
    private DatabaseTableRecord mockDatabaseTableRecord;

    @Mock
    private DatabaseTable mockDatabaseTable;

    @Mock
    private DatabaseTransaction mockTransaction;
    @Mock
    private Skin skin;
    @Mock
    private Object object;
    @Mock
    private XMLParser xmlParser = mock(XMLParser.class);


    String repoManifest = "<skin ></skin >";

    SubAppResourcesInstallationNetworkServicePluginRoot subAppResourcesInstallationNetworkServicePluginRoot;
    private SubAppResourcesInstallationNetworkServiceDAO subAppResourcesInstallationNetworkServiceDAO;
//
    @Before
    public void setUp() throws Exception {
        subAppResourcesInstallationNetworkServicePluginRoot = new SubAppResourcesInstallationNetworkServicePluginRoot();
        subAppResourcesInstallationNetworkServicePluginRoot.setPluginFileSystem(pluginFileSystem);
        subAppResourcesInstallationNetworkServicePluginRoot.setEventManager(mockEventManager);
        subAppResourcesInstallationNetworkServicePluginRoot.setErrorManager(errorManager);
        subAppResourcesInstallationNetworkServicePluginRoot.setPluginDatabaseSystem(mockPluginDatabaseSystem);

        when(mockPluginDatabaseSystem.openDatabase(any(UUID.class), anyString())).thenReturn(mockDatabase);
        when(githubConnection.getFile(anyString())).thenReturn(repoManifest);
        when(pluginFileSystem.createTextFile(any(UUID.class), anyString(), anyString(), any(FilePrivacy.class), any(FileLifeSpan.class))).thenReturn(layoutFile);
        when(pluginFileSystem.createBinaryFile(any(UUID.class), anyString(), anyString(), any(FilePrivacy.class), any(FileLifeSpan.class))).thenReturn(imageFile);

        when(mockEventManager.getNewListener(EventType.BEGUN_WALLET_INSTALLATION)).thenReturn(mockFermatEventListener);
        when(pluginFileSystem.getTextFile(any(UUID.class), anyString(), anyString(), any(FilePrivacy.class), any(FileLifeSpan.class))).thenReturn(mockPluginTextFile);
        setUpDataBase();
    }
    public void setUpDataBase() throws Exception {

        when(mockPluginDatabaseSystem.openDatabase(any(UUID.class), anyString())).thenReturn(mockDatabase);
        when(mockDatabaseFactory.newTableFactory(any(UUID.class), anyString())).thenReturn(mockFactoryTable);
        when(mockDatabase.getTable(anyString())).thenReturn(mockDatabaseTable);
        when(mockDatabaseTable.getEmptyRecord()).thenReturn(mockDatabaseTableRecord);
        when(mockDatabase.newTransaction()).thenReturn(mockTransaction);
        subAppResourcesInstallationNetworkServiceDAO = new SubAppResourcesInstallationNetworkServiceDAO(mockPluginDatabaseSystem);
        subAppResourcesInstallationNetworkServiceDAO = new SubAppResourcesInstallationNetworkServiceDAO(mockPluginDatabaseSystem);

        String path = "path1";
        String skinName = "skinName1";
        String navigationStructureVersion = "version1";
        repository = new Repository(skinName, navigationStructureVersion, path);

        subAppResourcesInstallationNetworkServiceDAO.initializeDatabase(UUID.randomUUID(), SubAppResourcesNetworkServiceDatabaseConstants.DATABASE_NAME);
        subAppResourcesInstallationNetworkServiceDAO.createRepository(repository, UUID.randomUUID());

    }

    @Test
    public void testInstallCompleteSubApp() throws Exception {
        subAppResourcesInstallationNetworkServicePluginRoot.start();
        catchException(subAppResourcesInstallationNetworkServicePluginRoot).installCompleteSubApp("wallet_factory",
                "bitDubai",
                "medium",
                "default",
                "en",
                "navigationStructureVersion",
                "");
        System.out.println(CatchException.<Exception>caughtException());
        assertThat(CatchException.<Exception>caughtException()).isNull();
    }

    @Test
    public void testInstallCompleteSubApp_FileNotFoundThrowsCantInstallCompleteSubAppResourcesException() throws Exception {
        subAppResourcesInstallationNetworkServicePluginRoot.start();
        catchException(subAppResourcesInstallationNetworkServicePluginRoot).installCompleteSubApp(null,
                "bitDubai",
                "medium",
                "default",
                "en",
                "navigationStructureVersion",
                "");
        System.out.println(CatchException.<Exception>caughtException());
        assertThat(CatchException.<Exception>caughtException()).isInstanceOf(CantInstallCompleteSubAppResourcesException.class);
    }
}
