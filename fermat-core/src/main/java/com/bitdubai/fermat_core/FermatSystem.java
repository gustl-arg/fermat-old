package com.bitdubai.fermat_core;

import com.bitdubai.fermat_api.layer.all_definition.common.system.abstract_classes.AbstractAddon;
import com.bitdubai.fermat_api.layer.all_definition.common.system.abstract_classes.AbstractPlatform;
import com.bitdubai.fermat_api.layer.all_definition.common.system.abstract_classes.AbstractPlugin;
import com.bitdubai.fermat_api.layer.all_definition.common.system.exceptions.CantCreateSystemException;
import com.bitdubai.fermat_api.layer.all_definition.common.system.exceptions.CantGetAddonException;
import com.bitdubai.fermat_api.layer.all_definition.common.system.exceptions.CantGetModuleManagerException;
import com.bitdubai.fermat_api.layer.all_definition.common.system.exceptions.CantRegisterPlatformException;
import com.bitdubai.fermat_api.layer.all_definition.common.system.exceptions.CantStartAddonException;
import com.bitdubai.fermat_api.layer.all_definition.common.system.exceptions.CantStartPluginException;
import com.bitdubai.fermat_api.layer.all_definition.common.system.exceptions.CantStartSystemException;
import com.bitdubai.fermat_api.layer.all_definition.common.system.exceptions.ModuleManagerNotFoundException;
import com.bitdubai.fermat_api.layer.all_definition.common.system.exceptions.VersionNotFoundException;
import com.bitdubai.fermat_api.layer.all_definition.common.system.utils.AddonVersionReference;
import com.bitdubai.fermat_api.layer.all_definition.common.system.utils.PlatformReference;
import com.bitdubai.fermat_api.layer.all_definition.common.system.utils.PluginVersionReference;
import com.bitdubai.fermat_api.layer.all_definition.enums.Platforms;
import com.bitdubai.fermat_api.layer.modules.ModuleManager;
import com.bitdubai.fermat_bch_core.BCHPlatform;
import com.bitdubai.fermat_cbp_core.CBPPlatform;
import com.bitdubai.fermat_ccp_core.CCPPlatform;
import com.bitdubai.fermat_dap_core.DAPPlatform;
import com.bitdubai.fermat_p2p_core.P2PPlatform;
import com.bitdubai.fermat_pip_core.PIPPlatform;
import com.bitdubai.fermat_wpd_core.WPDPlatform;

/**
 * The class <code>com.bitdubai.fermat_core.FermatSystem</code>
 * starts all the component of the platform and manage it.
 * <p/>
 * Created by Leon Acosta - (laion.cj91@gmail.com) on 22/10/2015.
 */
public final class FermatSystem {

    private final FermatSystemContext fermatSystemContext;
    private final FermatAddonManager  fermatAddonManager ;
    private final FermatPluginManager fermatPluginManager;

    /**
     * Through this Constructor, we aloud to create a new instance of the Fermat System, but we
     * should pass it an OS context and an OSA Platform.
     *
     * @param osContext      operative system context instance.
     * @param osaPlatform    OSA Platform instance.
     *
     * @throws CantCreateSystemException if something goes wrong.
     */
    public FermatSystem(final Object           osContext       ,
                        final AbstractPlatform osaPlatform) throws CantCreateSystemException {

        this.fermatSystemContext = new FermatSystemContext(osContext);
        this.fermatAddonManager  = new FermatAddonManager(fermatSystemContext);
        this.fermatPluginManager = new FermatPluginManager(fermatSystemContext, fermatAddonManager);

        try {

            this.registerOsaPlatform(osaPlatform);

        } catch (final CantRegisterPlatformException e) {

            throw new CantCreateSystemException(e, "", "Error registering OSA Platform.");
        }
    }

    /**
     * Here we start all the platforms of Fermat, one by one.
     * OSA Platform will be registered in the instantiation of fermat system class, this way we ensure that it will be a osa platform registered.
     *
     * @throws CantStartSystemException if something goes wrong.
     */
    public final void start() throws CantStartSystemException {

        try {

            fermatSystemContext.registerPlatform(new BCHPlatform());
            fermatSystemContext.registerPlatform(new CBPPlatform());
            fermatSystemContext.registerPlatform(new CCPPlatform());
            fermatSystemContext.registerPlatform(new DAPPlatform());
            fermatSystemContext.registerPlatform(new P2PPlatform());
            fermatSystemContext.registerPlatform(new PIPPlatform());
            fermatSystemContext.registerPlatform(new WPDPlatform());

        } catch(CantRegisterPlatformException e) {

            throw new CantStartSystemException(e, "", "There was a problem registering a Platform.");
        } catch(Exception e) {

            throw new CantStartSystemException(e, "", "Unhandled Exception.");
        }

    }

    /**
     * Through the method <code>registerOsaPlatform</code> we validate minimally that we're dealing with an osaPlatform.
     *
     * @param osaPlatform  instance of osa platform.
     *
     * @throws CantRegisterPlatformException if something goes wrong.
     */
    private void registerOsaPlatform(final AbstractPlatform osaPlatform) throws CantRegisterPlatformException {

        if (osaPlatform == null)
            throw new CantRegisterPlatformException("abstractPlatform=null", "You have pass through parameter an OSA Platform instance.");

        final PlatformReference pr = osaPlatform.getPlatformReference();

        if (pr.getPlatform() != null && pr.getPlatform().equals(Platforms.OPERATIVE_SYSTEM_API))
            fermatSystemContext.registerPlatform(osaPlatform);
        else
            throw new CantRegisterPlatformException(osaPlatform.getPlatformReference().toString(), "Is not referenced like an OSA specific Platform.");

    }

    /**
     * Through the method <code>getModuleManager</code> the graphic interface can access to the modules of
     * its sub-apps and wallets.
     *
     * @param pluginVersionReference plugin version reference data.
     *
     * @return an instance of the requested module manager.
     *
     * @throws CantGetModuleManagerException   if something goes wrong.
     * @throws ModuleManagerNotFoundException  if we can't find the requested module manager.
     */
    public final ModuleManager getModuleManager(final PluginVersionReference pluginVersionReference) throws CantGetModuleManagerException  ,

                                                                                                            ModuleManagerNotFoundException {

        try {

            AbstractPlugin moduleManager = fermatPluginManager.startPluginAndReferences(pluginVersionReference);

            if (moduleManager instanceof ModuleManager)
                return (ModuleManager) moduleManager;
            else
                throw new CantGetModuleManagerException(pluginVersionReference.toString(), "The plugin version requested not implements module manager interface.");

        } catch(VersionNotFoundException e) {

            throw new ModuleManagerNotFoundException(e, pluginVersionReference.toString(), "The module manager cannot be found.");
        } catch (Exception e) {

            throw new CantGetModuleManagerException(e, pluginVersionReference.toString(), "Unhandled error.");
        }
    }

    /**
     * Through the method <code>getAddon</code> the graphic interface can access to the addons of fermat
     *
     * @param addonVersionReference addon version reference data.
     *
     * @return an instance of the requested module manager.
     *
     * @throws CantGetAddonException      if something goes wrong.
     * @throws VersionNotFoundException   if we can't find the requested addon.
     */
    public final AbstractAddon getAddon(final AddonVersionReference addonVersionReference) throws VersionNotFoundException,
                                                                                                  CantGetAddonException   {

        try {

            return fermatAddonManager.startAddonAndReferences(addonVersionReference);

        } catch(CantStartAddonException e) {

            throw new CantGetAddonException(e, addonVersionReference.toString(), "The addon cannot be started.");
        } catch (Exception e) {

            throw new CantGetAddonException(e, addonVersionReference.toString(), "Unhandled error.");
        }
    }

    // TODO TEMPORAL METHOD
    public final AbstractPlugin getPluginVersion(final PluginVersionReference pluginVersionReference) throws VersionNotFoundException {

        return fermatSystemContext.getPluginVersion(pluginVersionReference);
    }

    // TODO TEMPORAL METHOD
    public final AbstractPlugin startAndGetPluginVersion(final PluginVersionReference pluginVersionReference) throws VersionNotFoundException ,
                                                                                                                     CantStartPluginException {

        return fermatPluginManager.startPluginAndReferences(pluginVersionReference);
    }

}
