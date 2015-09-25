package unit.com.bitdubait.fermat_ccp_plugin.layer.network_service.wallet_resources.developer.bitdubai.version_1.database.NetworkServicesWalletResourcesDAO;

import com.bitdubai.fermat_api.layer.osa_android.database_system.Database;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseFactory;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTable;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableFactory;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTableRecord;
import com.bitdubai.fermat_api.layer.osa_android.database_system.DatabaseTransaction;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_api.layer.osa_android.database_system.exceptions.DatabaseNotFoundException;
import com.bitdubai.fermat_ccp_plugin.layer.network_service.wallet_resources.developer.bitdubai.version_1.database.NetworkServicesWalletResourcesDAO;
import com.bitdubai.fermat_ccp_plugin.layer.network_service.wallet_resources.developer.bitdubai.version_1.database.NetworkserviceswalletresourcesDatabaseConstants;
import com.bitdubai.fermat_ccp_plugin.layer.network_service.wallet_resources.developer.bitdubai.version_1.structure.Repository;
import com.googlecode.catchexception.CatchException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;
import java.util.UUID;

import static com.googlecode.catchexception.CatchException.catchException;
import static org.fest.assertions.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * Created by natalia on 09/09/15.
 */

@RunWith(MockitoJUnitRunner.class)
public class DeleteTest {
    private NetworkServicesWalletResourcesDAO networkServicesWalletResourcesDAO;

    @Mock
    private Database mockDatabase;

    @Mock
    private PluginDatabaseSystem mockPluginDatabaseSystem;

    @Mock
    private DatabaseTableFactory mockFactoryTable;

    @Mock
    private DatabaseFactory mockDatabaseFactory;

    @Mock
    private DatabaseTableRecord mockDatabaseTableRecord;

    @Mock
    private DatabaseTable mockDatabaseTable;

    @Mock
    private List<DatabaseTableRecord> mockRecords;

    @Mock
    private DatabaseTableRecord mockRecord;


    private UUID testOwnerId;

    private UUID skinId;


    @Before
    public void setUp() throws Exception {
        testOwnerId = UUID.randomUUID();
        skinId = UUID.randomUUID();

        when(mockPluginDatabaseSystem.openDatabase(any(UUID.class), anyString())).thenReturn(mockDatabase);

        when(mockDatabase.getTable(anyString())).thenReturn(mockDatabaseTable);

        when(mockDatabaseTable.getRecords()).thenReturn(mockRecords);
        when(mockRecords.get(anyInt())).thenReturn(mockRecord);

        networkServicesWalletResourcesDAO = new NetworkServicesWalletResourcesDAO(mockPluginDatabaseSystem);
  networkServicesWalletResourcesDAO.initializeDatabase(testOwnerId, NetworkserviceswalletresourcesDatabaseConstants.DATABASE_NAME);
    }

    @Test
    public void deleteTest_Ok_ThrowsCantDeleteRepositoryException() throws Exception {

        catchException(networkServicesWalletResourcesDAO).delete(skinId, "repositorio1");
        assertThat(CatchException.<Exception>caughtException()).isNull();

    }

    @Test
    public void deleteTest_ErrorParamsNull_ThrowsCantDeleteRepositoryException() throws Exception {

         catchException(networkServicesWalletResourcesDAO).delete(null, null);
        assertThat(CatchException.<Exception>caughtException()).isNotNull();

    }



}