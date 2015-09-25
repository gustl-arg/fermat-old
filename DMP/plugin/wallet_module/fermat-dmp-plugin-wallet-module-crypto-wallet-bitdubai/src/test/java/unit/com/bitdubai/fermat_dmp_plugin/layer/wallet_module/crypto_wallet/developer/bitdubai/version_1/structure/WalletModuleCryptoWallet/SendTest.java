package unit.com.bitdubai.fermat_ccp_plugin.layer.wallet_module.crypto_wallet.developer.bitdubai.version_1.structure.WalletModuleCryptoWallet;

import com.bitdubai.fermat_api.layer.all_definition.crypto.asymmetric.AsymmectricCryptography;
import com.bitdubai.fermat_api.layer.all_definition.crypto.asymmetric.ECCKeyPair;
import com.bitdubai.fermat_api.layer.all_definition.enums.Actors;
import com.bitdubai.fermat_api.layer.all_definition.enums.CryptoCurrency;
import com.bitdubai.fermat_api.layer.all_definition.money.CryptoAddress;
import com.bitdubai.fermat_api.layer.ccp_middleware.wallet_contacts.interfaces.WalletContactsManager;
import com.bitdubai.fermat_api.layer.ccp_wallet_module.crypto_wallet.exceptions.CantSendCryptoException;
import com.bitdubai.fermat_api.layer.ccp_transaction.outgoing_extrauser.OutgoingExtraUserManager;
import com.bitdubai.fermat_api.layer.ccp_transaction.outgoing_extrauser.TransactionManager;
import com.bitdubai.fermat_api.layer.ccp_transaction.outgoing_extrauser.exceptions.CantGetTransactionManagerException;
import com.bitdubai.fermat_api.layer.ccp_transaction.outgoing_extrauser.exceptions.CantSendFundsException;
import com.bitdubai.fermat_api.layer.ccp_transaction.outgoing_extrauser.exceptions.InsufficientFundsException;
import com.bitdubai.fermat_cry_api.layer.crypto_module.crypto_address_book.interfaces.CryptoAddressBookManager;
import com.bitdubai.fermat_ccp_plugin.layer.wallet_module.crypto_wallet.developer.bitdubai.version_1.structure.CryptoWalletWalletModuleManager;
import com.bitdubai.fermat_pip_api.layer.pip_platform_service.error_manager.ErrorManager;

import junit.framework.TestCase;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

@RunWith(MockitoJUnitRunner.class)
public class SendTest extends TestCase {

    /**
     * DealsWithErrors interface Mocked
     */
    @Mock
    ErrorManager errorManager;

    /**
     * DealsWithCryptoAddressBook interface Mocked
     */
    @Mock
    CryptoAddressBookManager cryptoAddressBookManager;

    /**
     * DealsWithWalletContacts interface Mocked
     */
    @Mock
    WalletContactsManager walletContactsManager;

    /**
     * DealsWithOutgoingExtraUser interface Mocked
     */
    @Mock
    OutgoingExtraUserManager outgoingExtraUserManager;


    @Mock
    TransactionManager transactionManager;

    long cryptoAmount;
    CryptoAddress destinationAddress;
    String walletPublicKey;
    String notes;
    String deliveredByActorId;
    Actors deliveredByActorType;
    String deliveredToActorId;
    Actors deliveredToActorType;

    CryptoWalletWalletModuleManager walletModuleCryptoWallet;

    @Before
    public void setUp() throws Exception {
        cryptoAmount = 1;
        destinationAddress = new CryptoAddress("asdasd", CryptoCurrency.BITCOIN);
        walletPublicKey = AsymmectricCryptography.derivePublicKey(AsymmectricCryptography.createPrivateKey());
        notes = "NOTE";
        deliveredByActorId = new ECCKeyPair().getPublicKey();
        deliveredByActorType = Actors.EXTRA_USER;
        deliveredToActorId = new ECCKeyPair().getPublicKey();
        deliveredToActorType = Actors.INTRA_USER;

        walletModuleCryptoWallet = new CryptoWalletWalletModuleManager();
        walletModuleCryptoWallet.setErrorManager(errorManager);
        walletModuleCryptoWallet.setCryptoAddressBookManager(cryptoAddressBookManager);
        walletModuleCryptoWallet.setWalletContactsManager(walletContactsManager);
        walletModuleCryptoWallet.setOutgoingExtraUserManager(outgoingExtraUserManager);
        walletModuleCryptoWallet.initialize();
    }


    @Test
    public void testSend_Success() throws Exception {
        doReturn(transactionManager).when(outgoingExtraUserManager).getTransactionManager();
        walletModuleCryptoWallet.send(
                cryptoAmount,
                destinationAddress,
                notes,
                walletPublicKey,
                deliveredByActorId,
                deliveredByActorType,
                deliveredToActorId,
                deliveredToActorType
        );
    }

    @Ignore
    @Test(expected=CantSendCryptoException.class)
    public void testSend_InsufficientFundsException() throws Exception {
        doReturn(transactionManager).when(outgoingExtraUserManager).getTransactionManager();
        doThrow(new InsufficientFundsException(InsufficientFundsException.DEFAULT_MESSAGE, null, null, null))
        .when(transactionManager).send(
                anyString(),
                any(CryptoAddress.class),
                anyLong(),
                anyString(),
                anyString(),
                any(Actors.class),
                anyString(),
                any(Actors.class)
        );

        walletModuleCryptoWallet.send(cryptoAmount, destinationAddress, notes, walletPublicKey, deliveredByActorId, deliveredByActorType, deliveredToActorId, deliveredToActorType);
    }

    @Test(expected=CantSendCryptoException.class)
    public void testSend_CantGetTransactionManagerException() throws Exception {
        doThrow(new CantGetTransactionManagerException(CantGetTransactionManagerException.DEFAULT_MESSAGE, null, null, null))
            .when(outgoingExtraUserManager).getTransactionManager();

        walletModuleCryptoWallet.send(cryptoAmount, destinationAddress, notes, walletPublicKey, deliveredByActorId, deliveredByActorType, deliveredToActorId, deliveredToActorType);
    }

    @Test(expected=CantSendCryptoException.class)
    public void testSend_CantSendFundsException() throws Exception {
        doReturn(transactionManager).when(outgoingExtraUserManager).getTransactionManager();
        doThrow(new CantSendFundsException("MOCK", null, null, null))
                .when(transactionManager).send(
                anyString(),
                any(CryptoAddress.class),
                anyLong(),
                anyString(),
                anyString(),
                any(Actors.class),
                anyString(),
                any(Actors.class)
        );

        walletModuleCryptoWallet.send(cryptoAmount, destinationAddress, notes, walletPublicKey, deliveredByActorId, deliveredByActorType, deliveredToActorId, deliveredToActorType);
    }
}
