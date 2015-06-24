package com.bitdubai.reference_niche_wallet.bitcoin_wallet.fragments;


import android.app.AlertDialog;
import android.content.Context;

import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import com.bitdubai.android_fermat_dmp_wallet_bitcoin.R;
import com.bitdubai.fermat_api.layer.dmp_basic_wallet.bitcoin_wallet.BitcoinTransaction;
import com.bitdubai.fermat_api.layer.dmp_niche_wallet_type.crypto_wallet.exceptions.CantGetCryptoWalletException;
import com.bitdubai.fermat_api.layer.dmp_niche_wallet_type.crypto_wallet.exceptions.CantGetTransactionsException;
import com.bitdubai.fermat_api.layer.dmp_niche_wallet_type.crypto_wallet.interfaces.CryptoWallet;
import com.bitdubai.fermat_api.layer.dmp_niche_wallet_type.crypto_wallet.interfaces.CryptoWalletManager;
import com.bitdubai.reference_niche_wallet.bitcoin_wallet.Platform;

import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * Created by natalia on 17/06/15.
 */
public class TransactionsFragment extends Fragment {

    private static final String ARG_POSITION = "position";
    View rootView;
    public static Typeface mDefaultTypeface;

    List TRANSACTIONS = new ArrayList<Transactions>();

    /**
     * DealsWithNicheWalletTypeCryptoWallet Interface member variables.
     */
    private static CryptoWalletManager cryptoWalletManager;
    private static Platform platform = new Platform();
    CryptoWallet cryptoWallet;

    UUID wallet_id = UUID.fromString("25428311-deb3-4064-93b2-69093e859871");

    public static TransactionsFragment newInstance(int position) {
        TransactionsFragment f = new TransactionsFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try
        {


            cryptoWalletManager = platform.getCryptoWalletManager();

            try{
                cryptoWallet = cryptoWalletManager.getCryptoWallet();
            }
            catch (CantGetCryptoWalletException e) {
                showMessage("CantGetCryptoWalletException- " + e.getMessage());
                e.printStackTrace();
            }


            try {
                List<BitcoinTransaction> bitcoinTransactions = cryptoWallet.getTransactions(10, 10, wallet_id);

                for(int i = 0; i < bitcoinTransactions.size(); i++) {
                    BitcoinTransaction transaction = (BitcoinTransaction) bitcoinTransactions.get(i);
                    transaction.getAmount();
                    // Construct the data source
                    TRANSACTIONS.add(new Transactions(transaction.getAddressFrom().getAddress().toString(), String.valueOf(transaction.getTimestamp()), String.valueOf(transaction.getAmount()), transaction.getMemo(), transaction.getType().toString()));

                }

            }
            catch (CantGetTransactionsException e) {
                showMessage("Cant Get Transactions Exception- " + e.getMessage());
                e.printStackTrace();
            }

        }
        catch(Exception ex) {
            showMessage("Unexpected error get Transactions - " + ex.getMessage());
            ex.printStackTrace();
        }

        // Construct the data source
       // TRANSACTIONS.add(new Transactions("Lucia Alarcon De Zamacona", "4 hours ago", "0.0012", "New telephone","Send"));
        //TRANSACTIONS.add(new Transactions("Juan Luis R Pons","5 hours ago", "0.0023", "Old desk", "Received"));
        //TRANSACTIONS.add(new Transactions("Karina Rodríguez","yesterday 11:00 PM","0.1023","Car oil","Received"));

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.wallets_bitcoin_fragment_transactions, container, false);
        // Get ListView object from xml
        ListView listView = (ListView) rootView.findViewById(R.id.transactionlist);



// Create the adapter to convert the array to views
        TransactionArrayAdapter adapter = new TransactionArrayAdapter(this.getActivity().getApplicationContext(), TRANSACTIONS);



        // Assign adapter to ListView
        listView.setAdapter(adapter);


        return rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);




    }

    public class TransactionArrayAdapter extends ArrayAdapter<Transactions> {

        public TransactionArrayAdapter(Context context, List<Transactions> objects) {
            super(context, 0, objects);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){

            //get inflater
            LayoutInflater inflater = (LayoutInflater)getContext()
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);


            View listItemView = convertView;

            //if view exist
            if (null == convertView) {
                //Si no existe, entonces inflarlo con image_list_view.xml
                listItemView = inflater.inflate(
                        R.layout.wallets_bitcoin_fragment_transactions_list_items,
                        parent,
                        false);
            }

            //Get TextViews
            TextView contact_name = (TextView)listItemView.findViewById(R.id.contact_name);
            TextView amount = (TextView)listItemView.findViewById(R.id.amount);
            TextView notes = (TextView)listItemView.findViewById(R.id.notes);
            TextView when = (TextView)listItemView.findViewById(R.id.when);
            TextView type = (TextView)listItemView.findViewById(R.id.type);

            //Getting Transactions instance at the current position
            Transactions item = getItem(position);

            contact_name.setText(item.getName());
            amount.setText(item.getAmount());
            notes.setText(item.getMemo());
            when.setText(item.getDate());
            type.setText(item.getType());

            //return ListView
            return listItemView;

        }
    }




    public class Transactions{

        private String name;
        private String date;
        private String amount;
        private String memo;
        private String type;

        public Transactions(String name,String date,String amount, String memo,String type){
            this.name = name;
            this.date = date;
            this.amount=amount;
            this.memo=memo;
            this.type=type;

        }

        public void setname(String name){
            this.name = name;
        }

        public void setDate(String date){
            this.date = date;
        }

        public void setAmount(String amount){
            this.amount=amount;
        }

        public void setMemo(String memo){
            this.memo=memo;
        }

        public void setType(String type){
            this.type=type;
        }

        public String getName(){
            return this.name;}
        public String getDate(){return this.date;}
        public String getAmount() {
            return this.amount;
        }

        public String getMemo() {
            return this.memo;
        }

        public String getType() {
            return this.type;
        }

    }

    private void showMessage(String text){
        AlertDialog alertDialog = new AlertDialog.Builder(this.getActivity()).create();
        alertDialog.setTitle("Warning");
        alertDialog.setMessage(text);
        alertDialog.setButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // aquí puedes añadir funciones
            }
        });
        //alertDialog.setIcon(R.drawable.icon);
        alertDialog.show();
    }
}