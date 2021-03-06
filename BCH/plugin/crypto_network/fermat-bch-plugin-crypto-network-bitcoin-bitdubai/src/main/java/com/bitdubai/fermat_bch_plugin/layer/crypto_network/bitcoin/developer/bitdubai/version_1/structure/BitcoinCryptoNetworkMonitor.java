package com.bitdubai.fermat_bch_plugin.layer.crypto_network.bitcoin.developer.bitdubai.version_1.structure;

import com.bitdubai.fermat_api.layer.dmp_world.Agent;
import com.bitdubai.fermat_api.layer.dmp_world.wallet.exceptions.CantStartAgentException;
import com.bitdubai.fermat_api.layer.osa_android.database_system.PluginDatabaseSystem;
import com.bitdubai.fermat_bch_api.layer.crypto_network.bitcoin.exceptions.CantBroadcastTransactionException;
import com.bitdubai.fermat_bch_api.layer.crypto_network.bitcoin.interfaces.BitcoinNetworkConfiguration;
import com.bitdubai.fermat_bch_plugin.layer.crypto_network.bitcoin.developer.bitdubai.version_1.exceptions.BlockchainException;

import org.bitcoinj.core.BlockChain;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.PeerAddress;
import org.bitcoinj.core.PeerGroup;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.core.Wallet;
import org.bitcoinj.net.discovery.DnsDiscovery;
import org.bitcoinj.params.RegTestParams;

import java.net.InetSocketAddress;
import java.util.UUID;

/**
 * Created by rodrigo on 10/4/15.
 */
class BitcoinCryptoNetworkMonitor implements Agent {
    /**
     * agent execution flag
     */
    private boolean isSupposedToBeRunning = false;

    /**
     * class variables
     */
    Wallet wallet;
    BitcoinCryptoNetworkMonitorAgent bitcoinCryptoNetworkMonitorAgent;


    /**
     * Platform variables
     */
    PluginDatabaseSystem pluginDatabaseSystem;
    UUID plugId;

    /**
     * Constructor
     * @param pluginDatabaseSystem
     */
    public BitcoinCryptoNetworkMonitor(PluginDatabaseSystem pluginDatabaseSystem, UUID pluginId, Wallet wallet) {
        this.pluginDatabaseSystem = pluginDatabaseSystem;
        this.wallet = wallet;
        this.plugId = pluginId;
    }

    /**
     * Broadcast a well formed, commited and signed transaction into the network
     * @param tx
     * @throws CantBroadcastTransactionException
     */
    public void broadcastTransaction(Transaction tx) throws CantBroadcastTransactionException {
        bitcoinCryptoNetworkMonitorAgent.broadcastTransaction(tx);
    }

    @Override
    public void start() throws CantStartAgentException {
        isSupposedToBeRunning = true;

        /**
         * Then I will start the agent that connects to the bitcoin network to get new transactions
         */
        bitcoinCryptoNetworkMonitorAgent = new BitcoinCryptoNetworkMonitorAgent(this.wallet);
        try {
            bitcoinCryptoNetworkMonitorAgent.doTheMainTask();
        } catch (BlockchainException e) {
            e.printStackTrace();
        }
        // I have temporarelly removed the crypto network from the new thread.
        //Thread agentThread = new Thread(bitcoinCryptoNetworkMonitorAgent);
        //agentThread.start();
    }

    @Override
    public void stop() {
        isSupposedToBeRunning = false;
        /**
         * will wait until the peer agent stops.
         */
        while(bitcoinCryptoNetworkMonitorAgent.getPeerGroup().isRunning()){

        }

    }

    /**
     * Wallet setter
     * @param wallet
     */
    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }

    /**
     * Class that generates all the objects needed to connect to the network with the passed wallet.
     */
    private class BitcoinCryptoNetworkMonitorAgent implements Runnable{
        /**
         * class variables.
         */
        PeerGroup peerGroup;
        Wallet wallet;

        /**
         * sets this agent network type
         */
        final NetworkParameters NETWORK_PARAMETERS;

        /**
         * Constructor
         * @param wallet
         */
        public BitcoinCryptoNetworkMonitorAgent(Wallet wallet) {
            this.wallet = wallet;
            NETWORK_PARAMETERS = wallet.getNetworkParameters();
        }



        @Override
        public void run(){
            try {
                doTheMainTask();
            } catch (BlockchainException e) {
                e.printStackTrace();
            }

        }

        /**
         * Agent main method
         */
        private void doTheMainTask() throws BlockchainException {
            System.out.println("Crypto Network starting and connecting...");

            /**
             * creates the blockchain object for the specified network.
             */
            BitcoinCryptoNetworkBlockChain CryptoNetworkBlockChain = new BitcoinCryptoNetworkBlockChain(NETWORK_PARAMETERS);
            BlockChain blockChain = CryptoNetworkBlockChain.getBlockChain();
            blockChain.addWallet(this.wallet);

            /**
             * creates the peerGroup object
             */
            peerGroup = new PeerGroup(NETWORK_PARAMETERS, blockChain);
            peerGroup.addWallet(this.wallet);

            /**
             * add the events
             */
            BitcoinNetworkEvents events = new BitcoinNetworkEvents(pluginDatabaseSystem, plugId);
            peerGroup.addEventListener(events);
            this.wallet.addEventListener(events);

            /**
             * I will connect to the regTest server or search for peers if we are in a different network.
             */
            if (NETWORK_PARAMETERS == RegTestParams.get()){
                /**
                 * Peer 1
                 */
                InetSocketAddress inetSocketAddress1 = new InetSocketAddress(BitcoinNetworkConfiguration.BITCOIN_FULL_NODE_1_IP, BitcoinNetworkConfiguration.BITCOIN_FULL_NODE_1_PORT);
               PeerAddress peerAddress1 = new PeerAddress(inetSocketAddress1);
                peerGroup.addAddress(peerAddress1);

                /**
                 * Peer 2
                 */
                InetSocketAddress inetSocketAddress2 = new InetSocketAddress(BitcoinNetworkConfiguration.BITCOIN_FULL_NODE_2_IP, BitcoinNetworkConfiguration.BITCOIN_FULL_NODE_2_PORT);
                PeerAddress peerAddress2 = new PeerAddress(inetSocketAddress2);
                peerGroup.addAddress(peerAddress2);
            } else
                peerGroup.addPeerDiscovery(new DnsDiscovery(NETWORK_PARAMETERS));

            /**
             * Define internal agent information.
             */
            peerGroup.setUserAgent(BitcoinNetworkConfiguration.USER_AGENT_NAME, BitcoinNetworkConfiguration.USER_AGENT_VERSION);

            /**
             * starts the monitoring
             */
            peerGroup.start();
            peerGroup.startBlockChainDownload(null);

            while(true){

            }
        }

        /**
         * Broadcast a well formed, commited and signed transaction into the network
         * @param tx
         * @throws CantBroadcastTransactionException
         */
        public void broadcastTransaction(Transaction tx) throws CantBroadcastTransactionException {
            try{
                peerGroup.broadcastTransaction(tx).future().get();

            } catch (Exception e){
                throw new CantBroadcastTransactionException(CantBroadcastTransactionException.DEFAULT_MESSAGE, e, null, null);
            }

        }

        /**
         * gets the peer group
         * @return
         */
        public PeerGroup getPeerGroup() {
            return peerGroup;
        }
    }
}
