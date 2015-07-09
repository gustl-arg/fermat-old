package com.bitdubai.fermat_dmp_plugin.layer.transaction.incoming_extra_user.developer.bitdubai.version_1.util;

import com.bitdubai.fermat_api.layer.all_definition.enums.PlatformWalletType;
import com.bitdubai.fermat_api.layer.dmp_basic_wallet.bitcoin_wallet.exceptions.CantLoadWalletException;
import com.bitdubai.fermat_api.layer.dmp_basic_wallet.bitcoin_wallet.interfaces.BitcoinWalletManager;
import com.bitdubai.fermat_api.layer.dmp_basic_wallet.bitcoin_wallet.interfaces.BitcoinWalletWallet;
import com.bitdubai.fermat_dmp_plugin.layer.transaction.incoming_extra_user.developer.bitdubai.version_1.interfaces.TransactionExecutor;
import com.bitdubai.fermat_dmp_plugin.layer.transaction.incoming_extra_user.developer.bitdubai.version_1.structure.executors.BitcoinBasicWalletTransactionExecutor;

import java.util.UUID;

/**
 * Created by jorgegonzalez on 2015.07.08..
 */
public class TransactionExecutorFactory {

    private BitcoinWalletManager bitcoinWalletManager;

    public TransactionExecutorFactory(final BitcoinWalletManager bitcoinWalletManager){
        this.bitcoinWalletManager = bitcoinWalletManager;
    }

    public TransactionExecutor newTransactionExecutor(final PlatformWalletType walletType, final UUID walletId) throws CantLoadWalletException{
        switch (walletType){
            case BASIC_WALLET_BITCOIN_WALLET:
                return createBitcoinBasicWalletExecutor(walletId);
            default:
                return null;
        }
    }

    private TransactionExecutor createBitcoinBasicWalletExecutor(final UUID walletId) throws CantLoadWalletException {
        return new BitcoinBasicWalletTransactionExecutor(bitcoinWalletManager.loadWallet(walletId));
    }

}