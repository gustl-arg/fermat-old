package com.bitdubai.fermat_api.layer.ccp_module.wallet_manager.interfaces;

import com.bitdubai.fermat_api.layer.all_definition.enums.WalletCategory;
import com.bitdubai.fermat_api.layer.all_definition.util.Version;
import com.bitdubai.fermat_api.layer.ccp_engine.sub_app_runtime.enums.SubApps;
import com.bitdubai.fermat_api.layer.ccp_middleware.wallet_manager.interfaces.InstalledLanguage;
import com.bitdubai.fermat_api.layer.ccp_middleware.wallet_manager.interfaces.InstalledSkin;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Matias Furszyfer on 2015.08.19..
 */

public interface InstalledSubApp extends Serializable{

    /**
     * This method gives us the list of all the languages installed for this subApp
     *
     * @return the saud list of languages
     */
    public List<InstalledLanguage> getLanguagesId();

    /**
     * This method gives us the list of all the skins installed for this subApp
     *
     * @return the saud list of skins
     */
    public List<InstalledSkin> getSkinsId();

    /**
     * This method tell us the type of the subApp
     *
     * @return the subApp type
     */
    public SubApps getSubAppType();

    /**
     * This method gives us a codification of the wallet identifier (the identifier is an enum that
     * registers the subApp)
     *
     * @return an string that is result of the method getCode of an enum that can be inferred by the
     *         subApp
     */
    public String getSubAppPlatformIdentifier();

    /**
     * This method gives us the name of the wallet icon used to identify the image in the subApp resources plug-in
     *
     * @return the name of the said icon
     */
    public String getSubAppIcon();

    /**
     * This method gives us the public key of the wallet in this device. It is used as identifier of
     * the wallet
     *
     * @return the public key represented as a string
     */
    public String getSubAppPublicKey();

    /**
     * This method gives us the subApp name
     *
     * @return the name of the subApp
     */
    public String getSubAppName();

    /**
     * This method gives us the version of the subApp
     *
     * @return the version of the subApp
     */
    public Version getSubAppVersion();
}
