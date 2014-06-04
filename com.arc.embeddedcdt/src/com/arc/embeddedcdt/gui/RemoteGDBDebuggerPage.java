/*******************************************************************************
 * Copyright (c) 2006, 2010 PalmSource, Inc. and others.
 * All rights reserved. This program and the accompanying materials 
 * are made available under the terms of the Eclipse Public License v1.0 
 * which accompanies this distribution, and is available at 
 * http://www.eclipse.org/legal/epl-v10.html 
 * 
 * Contributors: 
 * Ewa Matejska (PalmSource)
 * 
 * Referenced GDBDebuggerPage code to write this.
 * Anna Dushistova (Mentor Graphics) - moved to org.eclipse.cdt.launch.remote.tabs
 * Synopsys, Inc. - ARC GNU Toolchain support
 *******************************************************************************/

package com.arc.embeddedcdt.gui;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import org.eclipse.cdt.debug.mi.internal.ui.GDBDebuggerPage;
import org.eclipse.cdt.internal.launch.remote.Messages;
import org.eclipse.cdt.launch.remote.IRemoteConnectionConfigurationConstants;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.internal.ui.SWTFactory;
import org.eclipse.debug.internal.ui.launchConfigurations.LaunchConfigurationsMessages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IViewReference;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.internal.Workbench;

import com.arc.embeddedcdt.LaunchConfigurationConstants;
import com.arc.embeddedcdt.launch.IMILaunchConfigurationConstants;
import com.arc.embeddedcdt.launch.Launch;

/**
 * The dynamic debugger tab for remote launches using gdb server.
 * The gdbserver settings are used to start a gdbserver session on the
 * remote and then to connect to it from the host. The DSDP-TM project is
 * used to accomplish this.
 */
public class RemoteGDBDebuggerPage extends GDBDebuggerPage {

	//protected Text fGDBServerCommandText;
	protected Combo fPrgmArgumentsComboInit;//this variable for select which externally tools
	protected static  Text fPrgmArgumentsTextInit;// this variable for showing  which target is be selected
	public static String  fPrgmArgumentsComboInittext=null; //this variable is for getting user's input initial command
	protected Text fGDBServerPortNumberText;
	protected Text fGDBServerIPAddressText;
	public String comport_openocd="";//this variable is for launching the exactly com port chosen by users
	public String comport_ashling="";//this variable is for launching the exactly com port chosen by users
	protected Button fSearchexternalButton;//this button is for searching the path for external tools
	protected Label fSearchexternalLabel;
	protected Button fLaunchComButton;//this variable is for launching COM port
	protected Button fLaunchernalButton;//this button is for launching the external tools
	protected Button fLaunchterminallButton;//this button is for launching the external tools
	protected Text fPrgmArgumentsTextexternal;//this button is for searching the path for external tools
	protected Combo fPrgmArgumentsComCom;//this variable is for getting user's input COM port
	private boolean fSerialPortAvailable = true;
	protected Label fPrgmArgumentsLabelCom;//this variable is for showing COM port
	
    static String runcom="";//this variable is for saving user's input run command
	public String external_openocd_path="";//this variable is for saving user's external path
	public String external_ashling_path="";//this variable is for saving user's external path
	public String external_nsim_path="";//this variable is for saving user's external path
	static String fLaunchexternal_openocd_Buttonboolean="true";//this variable is to get external tools current status (Enable/disable)
	static String fLaunchexternal_ashling_Buttonboolean="true";//this variable is to get external tools current status (Enable/disable)
	static String fLaunchexternal_nsim_Buttonboolean="true";//this variable is to get external tools current status (Enable/disable)
	static String fLaunchTerminalboolean="true";//this variable is to get external tools current status (Enable/disable)
	
	public Boolean createTabitemCOMBool=false;
	public Boolean createTabitemnSIMBool=false;
	
	public Boolean createTabitemCOMAshlingBool=false;
	// Constants
	public static final String ASHLING_DEFAULT_PATH_WINDOWS = "C:\\AshlingOpellaXDforARC";
	public static final String ASHLING_DEFAULT_PATH_LINUX = "/usr/bin";
	
	protected Label nSIMpropslabel;
	public static Text fnSIMpropsText;
	protected Button fnSIMpropslButton;//this button is for browsing the prop files for nSIM
	public static String nSIMpropsfiles=System.getenv("NSIM_HOME")
			 			+ java.io.File.separator + "systemc"
			 			+ java.io.File.separator + "configs"
			 			+ java.io.File.separator +
			 			"nsim_av2em11.props";
	public static String nSIMpropsfiles_last="";//this variable is for launching the exactly com port chosen by users
	
	
	protected Label nSIMtcflabel;
	public static Text fnSIMtcfText;
	protected Button fnSIMtcfButton;//this button is for browsing the tcf files for nSIM
	public static String nSIMtcffiles="";
	public static String nSIMtcffiles_last="";//this variable is for launching the exactly com port chosen by users
	
	
	public static String externaltools="";
	public static String externaltools_openocd_path="";
	public static String externaltools_ashling_path="";
	public static String externaltools_nsim_path="";

	@Override
	public String getName() {
		return Messages.Remote_GDB_Debugger_Options;
	}
	@Override
	public void setDefaults( ILaunchConfigurationWorkingCopy configuration ) {
		super.setDefaults(configuration);
		configuration.setAttribute( IRemoteConnectionConfigurationConstants.ATTR_GDBSERVER_COMMAND, 
									IRemoteConnectionConfigurationConstants.ATTR_GDBSERVER_COMMAND_DEFAULT );
		configuration.setAttribute( IRemoteConnectionConfigurationConstants.ATTR_GDBSERVER_PORT,
									IRemoteConnectionConfigurationConstants.ATTR_GDBSERVER_PORT_DEFAULT );
		configuration.setAttribute(LaunchConfigurationConstants.ATTR_DEBUGGER_EXTERNAL_TOOLS, (String) null);
		configuration.setAttribute(LaunchConfigurationConstants.ATTR_DEBUGGER_EXTERNAL_TOOLS_OPENOCD_PATH, (String) null);
		configuration.setAttribute(LaunchConfigurationConstants.ATTR_DEBUGGER_EXTERNAL_TOOLS_ASHLING_PATH, (String) null);
		configuration.setAttribute(LaunchConfigurationConstants.ATTR_DEBUGGER_EXTERNAL_TOOLS_NSIM_PATH, (String) null);
		configuration.setAttribute(LaunchConfigurationConstants.ATTR_DEBUGGER_TERMINAL_DEFAULT, (String) null);
		
	}
	
	/**
	 * Get default path to nSIM application nsimdrv.
	 */
	public static String getNsimdrvDefaultPath() {
	
		if (isWindowsOS()) {
			return System.getenv("NSIM_HOME") + java.io.File.separator
					+ "bin" + java.io.File.separator +
					"nsimdrv.exe";
		} else {
			return System.getenv("NSIM_HOME") + java.io.File.separator
					+ "bin" + java.io.File.separator +
					"nsimdrv";
		}
	}
	
	public static String getOpenOCDDefaultPath() {
		if (isWindowsOS()) {
			String s = System.getProperty("eclipse.home.location");
			s = s.substring("file:/".length()).replace("/", "\\");
			String path = s + java.io.File.separator +
					".." + java.io.File.separator +
					"share" + java.io.File.separator +
					"openocd" + java.io.File.separator +
					"scripts" + java.io.File.separator +
					"target" + java.io.File.separator +
					"snps_starter_kit_arc-em.cfg";
			try {
				return Paths.get(path).toRealPath().toString();
			} catch (IOException e) {
				e.printStackTrace();
				return "";
			}
		} else {
			return "/usr/local/share/openocd/scripts/target/snps_starter_kit_arc-em.cfg";
		}
	}

	@Override
	public void initializeFrom( ILaunchConfiguration configuration ) {
		createTabitemCOMBool=false;
		createTabitemCOMAshlingBool=false;
		createTabitemnSIMBool=false;
		super.initializeFrom(configuration);
		String gdbserverCommand = null;
		try {
			gdbserverCommand = configuration.getAttribute( IRemoteConnectionConfigurationConstants.ATTR_GDBSERVER_COMMAND,
														   IRemoteConnectionConfigurationConstants.ATTR_GDBSERVER_COMMAND_DEFAULT);
			
		}
		catch( CoreException e ) {
		}
		
		fGDBCommandText.setText( "arc-elf32-gdb" );
		try {
 		    externaltools = configuration.getAttribute(LaunchConfigurationConstants.ATTR_DEBUGGER_EXTERNAL_TOOLS, new String());
		    externaltools_openocd_path=configuration.getAttribute(LaunchConfigurationConstants.ATTR_DEBUGGER_EXTERNAL_TOOLS_OPENOCD_PATH, new String());
		    externaltools_ashling_path=configuration.getAttribute(LaunchConfigurationConstants.ATTR_DEBUGGER_EXTERNAL_TOOLS_ASHLING_PATH, new String());
		    externaltools_nsim_path=configuration.getAttribute(LaunchConfigurationConstants.ATTR_DEBUGGER_EXTERNAL_TOOLS_NSIM_PATH, new String());
		    
		    fLaunchexternal_openocd_Buttonboolean=configuration.getAttribute(LaunchConfigurationConstants.ATTR_DEBUGGER_EXTERNAL_OPENOCD_DEFAULT, "true");
		    fLaunchexternal_ashling_Buttonboolean=configuration.getAttribute(LaunchConfigurationConstants.ATTR_DEBUGGER_EXTERNAL_ASHLING_DEFAULT, "true");
		    fLaunchexternal_nsim_Buttonboolean=configuration.getAttribute(LaunchConfigurationConstants.ATTR_DEBUGGER_EXTERNAL_NSIM_DEFAULT, "true");
		    
		    comport_openocd=configuration.getAttribute(LaunchConfigurationConstants.ATTR_DEBUGGER_COM_OPENOCD_PORT, new String());
		    comport_ashling=configuration.getAttribute(LaunchConfigurationConstants.ATTR_DEBUGGER_COM_ASHLING_PORT, new String());
			nSIMpropsfiles_last = configuration.getAttribute(LaunchConfigurationConstants.ATTR_NSIM_PROP_FILE, new String());
		    
		if (configuration.getAttribute(LaunchConfigurationConstants.ATTR_DEBUGGER_EXTERNAL_TOOLS, new String()).equalsIgnoreCase(""))
		{
			fPrgmArgumentsComboInit.setText(fPrgmArgumentsComboInit.getItem(0));
		}
		else fPrgmArgumentsComboInit.setText(externaltools);	
		

		 // Set host and IP.
		 try {
			 String portnumber = configuration.getAttribute( IRemoteConnectionConfigurationConstants.ATTR_GDBSERVER_PORT,new String() );
			 if(portnumber.equalsIgnoreCase("")){
				 portnumber="3333"; 
			 }
     		 fGDBServerPortNumberText.setText( portnumber );
			 String hostname = configuration.getAttribute(LaunchConfigurationConstants.ATTR_DEBUGGER_GDB_ADDRESS, new String());
			 if(hostname.equalsIgnoreCase("")){
				 hostname = "localhost";
			 }
			 fGDBServerIPAddressText.setText(hostname);
			 

				
			 comport_openocd=configuration.getAttribute(LaunchConfigurationConstants.ATTR_DEBUGGER_COM_OPENOCD_PORT, new String());
			 if (FirstlaunchDialog.value[1] != null) {
					if (!FirstlaunchDialog.value[1].equalsIgnoreCase("")) {
						comport_openocd = FirstlaunchDialog.value[1];
					}

				}
			 comport_ashling=configuration.getAttribute(LaunchConfigurationConstants.ATTR_DEBUGGER_COM_ASHLING_PORT, new String());
			 if (FirstlaunchDialog.value[1] != null) {
					if (!FirstlaunchDialog.value[1].equalsIgnoreCase("")) {
						comport_ashling = FirstlaunchDialog.value[1];
					}

				}	 
			 //fnSIMpropsText.setText(nsimprop);
			 
			 
			
		 }
		 catch( CoreException e ) {
		 }
		
		 String Terminallaunch=configuration.getAttribute(LaunchConfigurationConstants.ATTR_DEBUGGER_TERMINAL_DEFAULT, new String());//get which external tool is in use

		String gdbserver=configuration.getAttribute(LaunchConfigurationConstants.ATTR_DEBUGGER_EXTERNAL_TOOLS, "JTAG via OpenOCD"/*new String()*/);
		
		 if(!gdbserver.equalsIgnoreCase(""))
		 {
			 int privious=fPrgmArgumentsComboInit.indexOf(gdbserver);
			 if(privious>-1)
				 fPrgmArgumentsComboInit.remove(privious);
			 fPrgmArgumentsComboInit.add(gdbserver, 0);
			 fPrgmArgumentsComboInit.select(0);
		 }
		
		} catch (CoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	@Override
	public void performApply( ILaunchConfigurationWorkingCopy configuration ) {
		super.performApply(configuration);
		//String str = fGDBServerCommandText.getText();
		//str.trim();
		//configuration.setAttribute( IRemoteConnectionConfigurationConstants.ATTR_GDBSERVER_COMMAND, str );
		String str = fGDBServerPortNumberText.getText();
		str.trim();
	
		configuration.setAttribute( IRemoteConnectionConfigurationConstants.ATTR_GDBSERVER_PORT, str );
		
		String gdbStr = fGDBCommandText.getText();
		gdbStr.trim();
		configuration.setAttribute(IMILaunchConfigurationConstants.ATTR_DEBUG_NAME, gdbStr);
		configuration.setAttribute(LaunchConfigurationConstants.ATTR_DEBUGGER_EXTERNAL_TOOLS,CommandTab.getAttributeValueFromString(fPrgmArgumentsComboInit.getItem(0)));
		configuration.setAttribute(LaunchConfigurationConstants.ATTR_DEBUGGER_EXTERNAL_TOOLS_OPENOCD_PATH,external_openocd_path);
		configuration.setAttribute(LaunchConfigurationConstants.ATTR_DEBUGGER_EXTERNAL_TOOLS_ASHLING_PATH,external_ashling_path);
		configuration.setAttribute(LaunchConfigurationConstants.ATTR_DEBUGGER_EXTERNAL_TOOLS_NSIM_PATH,external_nsim_path);
		
		configuration.setAttribute(LaunchConfigurationConstants.ATTR_DEBUGGER_EXTERNAL_TOOLS,getAttributeValueFromString(fPrgmArgumentsComboInittext));
		
		configuration.setAttribute(LaunchConfigurationConstants.ATTR_DEBUGGER_EXTERNAL_OPENOCD_DEFAULT,getAttributeValueFromString(fLaunchexternal_openocd_Buttonboolean));
		configuration.setAttribute(LaunchConfigurationConstants.ATTR_DEBUGGER_EXTERNAL_ASHLING_DEFAULT,getAttributeValueFromString(fLaunchexternal_ashling_Buttonboolean));
		configuration.setAttribute(LaunchConfigurationConstants.ATTR_DEBUGGER_EXTERNAL_NSIM_DEFAULT,getAttributeValueFromString(fLaunchexternal_nsim_Buttonboolean));
		
		configuration.setAttribute(LaunchConfigurationConstants.ATTR_NSIM_PROP_FILE,nSIMpropsfiles_last);
		if (fSerialPortAvailable)
			configuration.setAttribute(LaunchConfigurationConstants.ATTR_DEBUGGER_TERMINAL_DEFAULT,getAttributeValueFromString(fLaunchTerminalboolean));
		
		String hostname = fGDBServerIPAddressText.getText();
		configuration.setAttribute(
				LaunchConfigurationConstants.ATTR_DEBUGGER_GDB_ADDRESS,
				getAttributeValueFromString(hostname)
		);
	
		configuration.setAttribute(LaunchConfigurationConstants.ATTR_DEBUGGER_COM_OPENOCD_PORT,getAttributeValueFromString(comport_openocd));
		configuration.setAttribute(LaunchConfigurationConstants.ATTR_DEBUGGER_COM_ASHLING_PORT,getAttributeValueFromString(comport_ashling));
		
	}
	/* 
	* @return true---windows 
	*/
	public static boolean isWindowsOS(){
	    boolean isWindowsOS = false;
	    String osName = System.getProperty("os.name");
	    if(osName.toLowerCase().indexOf("windows")>-1){
	      isWindowsOS = true;
	    }
	    return isWindowsOS;
	 }

	static Group groupcom;
	static Group groupcomashling;
	static Group groupnsim;
	
	protected void createGdbserverSettingsTab( TabFolder tabFolder ) {
		TabItem tabItem = new TabItem( tabFolder, SWT.NONE );
		tabItem.setText( Messages.Gdbserver_Settings_Tab_Name );
		
		Composite comp = new Composite(tabFolder, SWT.NULL);
		comp.setLayout(new GridLayout(1, true));
		comp.setLayoutData(new GridData(GridData.FILL_BOTH));
		((GridLayout)comp.getLayout()).makeColumnsEqualWidth = false;
		comp.setFont( tabFolder.getFont() );
		tabItem.setControl( comp );
		
		final Composite subComp = new Composite(comp, SWT.NULL);
		subComp.setLayout(new GridLayout(5, true));
		subComp.setLayoutData(new GridData(GridData.FILL_BOTH));
		((GridLayout)subComp.getLayout()).makeColumnsEqualWidth = false;
		subComp.setFont( tabFolder.getFont() );
		
	
	
	
		
		Label label = new Label(subComp, SWT.LEFT);		
		label.setText("ARC GDB Server:");
		GridData gd = new GridData();
		label.setLayoutData( gd );
		gd = new GridData();
		gd.horizontalSpan =4;
		fPrgmArgumentsComboInit =new Combo(subComp, SWT.None|SWT.READ_ONLY);//1-2 and 1-3
		fPrgmArgumentsComboInit.setLayoutData(gd);
		fPrgmArgumentsComboInit.add("JTAG via OpenOCD");
		fPrgmArgumentsComboInit.add("JTAG via Ashling");
		fPrgmArgumentsComboInit.add("nSIM");
		fPrgmArgumentsComboInit.add("Generic gdbserver");
		
		fPrgmArgumentsComboInit.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent evt) {
				Combo combo= (Combo)evt.widget;
				boolean isWindows=isWindowsOS();
				fGDBServerPortNumberText.getText();
				//fGDBServerIPAddressText.setText("localhost");
				fPrgmArgumentsComboInittext = combo.getText();
						    
				if (fPrgmArgumentsComboInittext
						.equalsIgnoreCase("JTAG via OpenOCD")) {
					fGDBServerPortNumberText.setText("3333");
					
					
					groupnsim.dispose();
					groupcomashling.dispose();
					
					if(createTabitemCOMBool==false)
                    {
						if (!groupcom.isDisposed())
							groupcom.dispose();

						createTabitemCOM(subComp);
						if (externaltools_openocd_path.equalsIgnoreCase(""))
							fPrgmArgumentsTextexternal.setText(getOpenOCDDefaultPath());
						else
							fPrgmArgumentsTextexternal.setText(externaltools_openocd_path);
						
						 if(!comport_openocd.equalsIgnoreCase(""))
						 {
							 int privious=fPrgmArgumentsComCom.indexOf(comport_openocd);
							 if(privious>-1)
							     fPrgmArgumentsComCom.remove(privious);
							 fPrgmArgumentsComCom.add(comport_openocd, 0);
							 
							 
						 }
						 fPrgmArgumentsComCom.setText(fPrgmArgumentsComCom.getItem(0));
						 
						if (fLaunchexternal_openocd_Buttonboolean.equalsIgnoreCase("true")) {
							fLaunchernalButton.setSelection(true);
							fSearchexternalButton.setEnabled(true);
							fSearchexternalLabel.setEnabled(true);
							fPrgmArgumentsTextexternal.setEnabled(true);
						} else {
							fLaunchernalButton.setSelection(false);
							fSearchexternalButton.setEnabled(false);
							fSearchexternalLabel.setEnabled(false);
							fPrgmArgumentsTextexternal.setEnabled(false);
						}					
					}
					groupcom.setText("JTAG via OpenOCD");
					createTabitemnSIMBool=false;
					createTabitemCOMAshlingBool=false;
					
					if(!groupcom.isVisible())
						groupcom.setVisible(true);
					
					
					
				}
				else if(fPrgmArgumentsComboInittext.equalsIgnoreCase("JTAG via Ashling"))
				{
					fGDBServerPortNumberText.setText("2331");
					
					
					groupnsim.dispose();
					groupcom.dispose();
					createTabitemnSIMBool=false;
					createTabitemCOMBool=false;
					
					if(createTabitemCOMAshlingBool==false){
						if (!groupcomashling.isDisposed())
							groupcomashling.dispose();

						createTabitemCOMAshling(subComp);
						String defaultValue = isWindowsOS() ? ASHLING_DEFAULT_PATH_WINDOWS : ASHLING_DEFAULT_PATH_LINUX;
						
						if(externaltools_ashling_path.equalsIgnoreCase(""))
							fPrgmArgumentsTextexternal.setText(defaultValue);
						else 
							fPrgmArgumentsTextexternal.setText(externaltools_ashling_path);
						
						 if(!comport_ashling.equalsIgnoreCase(""))
						 {
							 int privious=fPrgmArgumentsComCom.indexOf(comport_ashling);
							 if(privious>-1)
							     fPrgmArgumentsComCom.remove(privious);
							 fPrgmArgumentsComCom.add(comport_ashling, 0);
							 
							 
						 }
						 fPrgmArgumentsComCom.setText(fPrgmArgumentsComCom.getItem(0));
						 
						if (fLaunchexternal_ashling_Buttonboolean.equalsIgnoreCase("true")) {
							fLaunchernalButton.setSelection(true);
							fSearchexternalButton.setEnabled(true);
							fSearchexternalLabel.setEnabled(true);
							fPrgmArgumentsTextexternal.setEnabled(true);
						} else {
							fLaunchernalButton.setSelection(false);
							fSearchexternalButton.setEnabled(false);
							fSearchexternalLabel.setEnabled(false);
							fPrgmArgumentsTextexternal.setEnabled(false);
						}
					}
						 
					groupcomashling.setText("JTAG via Ashling");
    				if(!groupcomashling.isVisible())
						groupcomashling.setVisible(true);
				}
				else if(fPrgmArgumentsComboInittext.equalsIgnoreCase("nSIM"))
				{
					
					fGDBServerPortNumberText.setText("1234");
				
					if (!CommandTab.initcom.isEmpty())
						CommandTab.initcom="";

					fLaunchTerminalboolean="false";

					
					IWorkbenchPage page = Workbench.getInstance().getActiveWorkbenchWindow().getActivePage();

					String viewId = "org.eclipse.tm.terminal.view.TerminalView"; 

					if (page != null) {
						IViewReference[] viewReferences = page.getViewReferences();
						for (IViewReference ivr : viewReferences) {
							if (ivr.getId().equalsIgnoreCase(viewId)
									|| ivr.getId().equalsIgnoreCase("more view id if you want to close more than one at a time")) {
								page.hideView(ivr);
							}
						}
					}
					
					groupcom.dispose();
					groupcomashling.dispose();
					if(createTabitemnSIMBool==false){
						if(!groupnsim.isDisposed())
						    groupnsim.dispose();
					    createTabitemnSIM(subComp);
					    if(externaltools_nsim_path.equalsIgnoreCase(""))
					    	fPrgmArgumentsTextexternal.setText(getNsimdrvDefaultPath());
						
						else
						     fPrgmArgumentsTextexternal.setText(externaltools_nsim_path);
					    
					    if(nSIMpropsfiles_last.equalsIgnoreCase(""))
					    {
					    	fnSIMpropsText.setText(nSIMpropsfiles);
					    	nSIMpropsfiles_last=nSIMpropsfiles;
					    }
					    	
					    else 
					    	fnSIMpropsText.setText(nSIMpropsfiles_last);
					    
						if (fLaunchexternal_nsim_Buttonboolean.equalsIgnoreCase("true")) {
							fLaunchernalButton.setSelection(true);
							fSearchexternalButton.setEnabled(true);
							fSearchexternalLabel.setEnabled(true);
							fPrgmArgumentsTextexternal.setEnabled(true);
						} else {
							fLaunchernalButton.setSelection(false);
							fSearchexternalButton.setEnabled(false);
							fSearchexternalLabel.setEnabled(false);
							fPrgmArgumentsTextexternal.setEnabled(false);
						}
					    
					}
					groupnsim.setText("nSIM");
					createTabitemCOMBool=false;
					createTabitemCOMAshlingBool=false;
					if(!groupnsim.isVisible())
						groupnsim.setVisible(true);
					
					
				}
				else if(fPrgmArgumentsComboInittext.equalsIgnoreCase("Generic gdbserver"))
				{

					fLaunchTerminalboolean="false";

					IWorkbenchPage page = Workbench.getInstance().getActiveWorkbenchWindow().getActivePage();

					String viewId = "org.eclipse.tm.terminal.view.TerminalView"; 

					if (page != null) {
						IViewReference[] viewReferences = page.getViewReferences();
						for (IViewReference ivr : viewReferences) {
							if (ivr.getId().equalsIgnoreCase(viewId)
									|| ivr.getId().equalsIgnoreCase("more view id if you want to close more than one at a time")) {
								page.hideView(ivr);
							}
						}
					}

					if(!groupcom.isDisposed())
					      groupcom.setVisible(false);
					if(!groupnsim.isDisposed())
					    groupnsim.setVisible(false);
					if(!groupcomashling.isDisposed())
						groupcomashling.setVisible(false);
					
				}

				updateLaunchConfigurationDialog();

			
			}
			});
		
		label = new Label(subComp, SWT.LEFT);
		label.setText(Messages.Port_number_textfield_label);
		gd = new GridData();
		gd.horizontalSpan =1;
		label.setLayoutData( gd );
		
		fGDBServerPortNumberText = new Text(subComp, SWT.SINGLE | SWT.BORDER| SWT.BEGINNING);
		gd = new GridData();
		gd.horizontalSpan =4;
		fGDBServerPortNumberText.setLayoutData(gd);
		fGDBServerPortNumberText.addModifyListener( new ModifyListener() {

			public void modifyText( ModifyEvent evt ) {
				updateLaunchConfigurationDialog();
				//portnumber=fGDBServerPortNumberText.getText();
			}
		} );
		
		label = new Label(subComp, SWT.LEFT);
		label.setText("Host Address:");
		gd = new GridData();
		gd.horizontalSpan =1;
		label.setLayoutData( gd );
		fGDBServerIPAddressText = new Text(subComp, SWT.SINGLE | SWT.BORDER| SWT.BEGINNING);
		gd = new GridData();
		gd.horizontalSpan =4;
		fGDBServerIPAddressText.setLayoutData( gd );
		fGDBServerIPAddressText.setText("localhost");
		fGDBServerIPAddressText.addModifyListener( new ModifyListener() {

			public void modifyText( ModifyEvent evt ) {
				updateLaunchConfigurationDialog();
			}
		} );
		
				

        if(createTabitemnSIMBool==false)
        	createTabitemnSIM(subComp);
        if(createTabitemCOMBool==false)
        	createTabitemCOM(subComp);
        if(createTabitemCOMAshlingBool==false)
        	createTabitemCOMAshling(subComp);
		
		
		
	}
	
private void createTabitemCOMAshling(Composite subComp) { 
	    createTabitemCOMAshlingBool=true;
		groupcomashling = SWTFactory.createGroup(subComp, fPrgmArgumentsComboInit.getItem(0), 5, 5, GridData.FILL_HORIZONTAL);
		Composite compCOM = SWTFactory.createComposite(groupcomashling, 5, 5, GridData.FILL_BOTH);
		
	
		fSearchexternalLabel=new Label(compCOM, SWT.LEFT);
		fSearchexternalLabel.setText("Ashling Path");
		GridData gd = new GridData();
		fSearchexternalLabel.setLayoutData(gd);
			
		fPrgmArgumentsTextexternal=new Text(compCOM, SWT.SINGLE | SWT.BORDER);//6-1
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint=400;
		gd.horizontalSpan =2;
		fPrgmArgumentsTextexternal.setLayoutData(gd);
		fPrgmArgumentsTextexternal.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent evt) {
				external_ashling_path=fPrgmArgumentsTextexternal.getText();
				updateLaunchConfigurationDialog();
			}
		});
		String defaultValue = isWindowsOS() ? ASHLING_DEFAULT_PATH_WINDOWS : ASHLING_DEFAULT_PATH_LINUX;
		
		if(externaltools_ashling_path.equalsIgnoreCase(""))
			fPrgmArgumentsTextexternal.setText(defaultValue);
		else 
			fPrgmArgumentsTextexternal.setText(externaltools_ashling_path);
		
		
		
		
		fSearchexternalButton = createPushButton(compCOM, "Browse", null); //$NON-NLS-1$  //6-2
		gd = new GridData(SWT.BEGINNING);
		fSearchexternalButton.setLayoutData(gd);
		fSearchexternalButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent evt) {
				handleBinaryBrowseButtonSelected();
				updateLaunchConfigurationDialog();
			}
		});
	
		fLaunchernalButton = new Button(compCOM,SWT.CHECK); //$NON-NLS-1$ //6-3
		fLaunchernalButton.setSelection(true);
		gd = new GridData(SWT.BEGINNING);
		fLaunchernalButton.setLayoutData(gd);
		fLaunchernalButton.setText("Launch Ashling");
		fLaunchernalButton.addSelectionListener(new SelectionListener() {

	        public void widgetSelected(SelectionEvent event) {
	        	if(fLaunchernalButton.getSelection()==true){
	        	fLaunchexternal_ashling_Buttonboolean="true";
	        	fSearchexternalButton.setEnabled(true);
	        	fSearchexternalLabel.setEnabled(true);
	        	fPrgmArgumentsTextexternal.setEnabled(true);
	        	}
	        	else {
	        		fLaunchexternal_ashling_Buttonboolean="false";
		        	fSearchexternalButton.setEnabled(false);
		        	fSearchexternalLabel.setEnabled(false);
		        	fPrgmArgumentsTextexternal.setEnabled(false);
	           	}
	        	updateLaunchConfigurationDialog();
	        }

	        public void widgetDefaultSelected(SelectionEvent event) {
	        }
	        
	      });
		
		
		fPrgmArgumentsLabelCom = new Label(compCOM, SWT.NONE);//5-1
		fPrgmArgumentsLabelCom.setText("COM  Ports:"); //$NON-NLS-1$
	
	
		fPrgmArgumentsComCom =new Combo(compCOM, SWT.None);//5-2 and 5-3 
		
		
		
		
		fLaunchComButton = new Button(compCOM,SWT.CHECK); //$NON-NLS-1$ //6-3
		fLaunchComButton.setSelection(true);
	
	
		fLaunchComButton.setText("Launch Terminal");
		fLaunchComButton.addSelectionListener(new SelectionListener() {
	        public void widgetSelected(SelectionEvent event) {
	        	if(fLaunchComButton.getSelection()==true){
	        		fLaunchTerminalboolean="true";
	        		fPrgmArgumentsComCom.setEnabled(fSerialPortAvailable);
	        		fPrgmArgumentsLabelCom.setEnabled(fSerialPortAvailable);
	        	} else {
	        		fLaunchTerminalboolean="false";
		        	fPrgmArgumentsComCom.setEnabled(false);
		        	fPrgmArgumentsLabelCom.setEnabled(false);
	           	}
	        	updateLaunchConfigurationDialog();
	        }

	        public void widgetDefaultSelected(SelectionEvent event) {
	        }
	        
	      });
		
		// Set serial port list. This call might fail, for example if RxTx
		// library is not available. In this case let's just disable UI elements.
		List<String> COM = null;
		try {
			COM = Launch.COMserialport();
		} catch (java.lang.UnsatisfiedLinkError e) {
			e.printStackTrace();
		} catch (java.lang.NoClassDefFoundError e) {
			e.printStackTrace();
		}

		if (COM != null) {
			for (int ii=0;ii<COM.size();ii++) {
				    String currentcom=(String) COM.get(ii);
				    fPrgmArgumentsComCom.add(currentcom);
	        }
		} 
		else {
			fSerialPortAvailable = false;
			fPrgmArgumentsComCom.setEnabled(fSerialPortAvailable);
			fLaunchComButton.setEnabled(fSerialPortAvailable);
		}
		
		
		fPrgmArgumentsComCom.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent evt) {
				Combo combo= (Combo)evt.widget;
				comport_ashling=combo.getText();
				updateLaunchConfigurationDialog();
			}
		});
		
		 if(!comport_ashling.equalsIgnoreCase(""))
		 {
			 int privious=fPrgmArgumentsComCom.indexOf(comport_ashling);
			 if(privious>-1)
			     fPrgmArgumentsComCom.remove(privious);
			 fPrgmArgumentsComCom.add(comport_ashling, 0);
			 
			 
		 }
		 fPrgmArgumentsComCom.setText(fPrgmArgumentsComCom.getItem(0));
		
	}
	
	private void createTabitemCOM(Composite subComp) { 
		createTabitemCOMBool=true;
		
		groupcom = SWTFactory.createGroup(subComp, fPrgmArgumentsComboInit.getItem(0), 5, 5, GridData.FILL_HORIZONTAL);
		Composite compCOM = SWTFactory.createComposite(groupcom, 5, 5, GridData.FILL_BOTH);
	
		
		fSearchexternalLabel=new Label(compCOM, SWT.LEFT);
		fSearchexternalLabel.setText("OpenOCD Path");
		GridData gd = new GridData();
		fSearchexternalLabel.setLayoutData(gd);
			
		fPrgmArgumentsTextexternal=new Text(compCOM, SWT.SINGLE | SWT.BORDER);//6-1
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint=400;
		gd.horizontalSpan =2;
		fPrgmArgumentsTextexternal.setLayoutData(gd);
		fPrgmArgumentsTextexternal.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent evt) {
				external_openocd_path=fPrgmArgumentsTextexternal.getText();
				updateLaunchConfigurationDialog();
			}
		});
		if(externaltools_openocd_path.equalsIgnoreCase(""))
		     fPrgmArgumentsTextexternal.setText(getOpenOCDDefaultPath());
		else  fPrgmArgumentsTextexternal.setText(externaltools_openocd_path);
		
		
		fSearchexternalButton = createPushButton(compCOM, "Browse", null); //$NON-NLS-1$  //6-2
		gd = new GridData(SWT.BEGINNING);
		fSearchexternalButton.setLayoutData(gd);
		fSearchexternalButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent evt) {
				handleBinaryBrowseButtonSelected();
				updateLaunchConfigurationDialog();
			}
		});
	
		fLaunchernalButton = new Button(compCOM,SWT.CHECK); //$NON-NLS-1$ //6-3
		fLaunchernalButton.setSelection(true);
		gd = new GridData(SWT.BEGINNING);
		fLaunchernalButton.setLayoutData(gd);
		fLaunchernalButton.setText("Launch OpenOCD");
		fLaunchernalButton.addSelectionListener(new SelectionListener() {

	        public void widgetSelected(SelectionEvent event) {
	        	if(fLaunchernalButton.getSelection()==true){
	        	fLaunchexternal_openocd_Buttonboolean="true";
	        	fSearchexternalButton.setEnabled(true);
	        	fSearchexternalLabel.setEnabled(true);
	        	fPrgmArgumentsTextexternal.setEnabled(true);
	        	}
	        	else {
		        	fLaunchexternal_openocd_Buttonboolean="false";
		        	fSearchexternalButton.setEnabled(false);
		        	fSearchexternalLabel.setEnabled(false);
		        	fPrgmArgumentsTextexternal.setEnabled(false);
	           	}
	        	updateLaunchConfigurationDialog();
	        }

	        public void widgetDefaultSelected(SelectionEvent event) {
	        }
	        
	      });
		
			
		fPrgmArgumentsLabelCom = new Label(compCOM, SWT.NONE);//5-1
		fPrgmArgumentsLabelCom.setText("COM  Ports:"); //$NON-NLS-1$
	    fPrgmArgumentsComCom =new Combo(compCOM, SWT.None);//5-2 and 5-3 
		fLaunchComButton = new Button(compCOM,SWT.CHECK); //$NON-NLS-1$ //6-3
		fLaunchComButton.setSelection(true);
	
	
		fLaunchComButton.setText("Launch Terminal");
		fLaunchComButton.addSelectionListener(new SelectionListener() {
	        public void widgetSelected(SelectionEvent event) {
	        	if(fLaunchComButton.getSelection()==true){
	        		fLaunchTerminalboolean="true";
	        		fPrgmArgumentsComCom.setEnabled(fSerialPortAvailable);
	        		fPrgmArgumentsLabelCom.setEnabled(fSerialPortAvailable);
	        	} else {
	        		fLaunchTerminalboolean="false";
		        	fPrgmArgumentsComCom.setEnabled(false);
		        	fPrgmArgumentsLabelCom.setEnabled(false);
	           	}
	        	updateLaunchConfigurationDialog();
	        }

	        public void widgetDefaultSelected(SelectionEvent event) {
	        }
	        
	      });
		
		// Set serial port list. This call might fail, for example if RxTx
		// library is not available. In this case let's just disable UI elements.
		List<String> COM = null;
		try {
			COM = Launch.COMserialport();
		} catch (java.lang.UnsatisfiedLinkError e) {
			e.printStackTrace();
		} catch (java.lang.NoClassDefFoundError e) {
			e.printStackTrace();
		}

		if (COM != null) {
			for (int ii=0;ii<COM.size();ii++) {
				    String currentcom=(String) COM.get(ii);
				    fPrgmArgumentsComCom.add(currentcom);
	        }
		} 
		else {
			fSerialPortAvailable = false;
			fPrgmArgumentsComCom.setEnabled(fSerialPortAvailable);
			fLaunchComButton.setEnabled(fSerialPortAvailable);
		}
		
		
		fPrgmArgumentsComCom.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent evt) {
				Combo combo= (Combo)evt.widget;
				comport_openocd=combo.getText();
				updateLaunchConfigurationDialog();
			}
		});
		
		 if(!comport_openocd.equalsIgnoreCase(""))
		 {
			 int privious=fPrgmArgumentsComCom.indexOf(comport_openocd);
			 if(privious>-1)
			     fPrgmArgumentsComCom.remove(privious);
			 fPrgmArgumentsComCom.add(comport_openocd, 0);
			 
			 
		 }
		 fPrgmArgumentsComCom.setText(fPrgmArgumentsComCom.getItem(0));
		
	}
	private void createTabitemnSIM(Composite subComp) { 
		createTabitemnSIMBool=true;

		groupnsim = SWTFactory.createGroup(subComp, fPrgmArgumentsComboInit.getItem(0), 5, 5, GridData.FILL_HORIZONTAL);
		@SuppressWarnings("restriction")
		Composite compnSIM = SWTFactory.createComposite(groupnsim, 5, 5, GridData.FILL_BOTH);
		
		fSearchexternalLabel=new Label(compnSIM, SWT.LEFT);
		fSearchexternalLabel.setText("nSIM Path");
		GridData gd = new GridData();
		fSearchexternalLabel.setLayoutData(gd);
			
		fPrgmArgumentsTextexternal=new Text(compnSIM, SWT.SINGLE | SWT.BORDER);//6-1
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint=400;
		gd.horizontalSpan =2;
		fPrgmArgumentsTextexternal.setLayoutData(gd);
		fPrgmArgumentsTextexternal.addModifyListener(new ModifyListener() {
			public void modifyText(ModifyEvent evt) {
				external_nsim_path=fPrgmArgumentsTextexternal.getText();
				updateLaunchConfigurationDialog();
			}
		});
		
		if(externaltools_nsim_path.equalsIgnoreCase(""))
	    	fPrgmArgumentsTextexternal.setText(getNsimdrvDefaultPath());
		
		else
		    	fPrgmArgumentsTextexternal.setText(externaltools_nsim_path);
		
		fSearchexternalButton = createPushButton(compnSIM, "Browse", null); //$NON-NLS-1$  //6-2
		gd = new GridData(SWT.BEGINNING);
		fSearchexternalButton.setLayoutData(gd);
		fSearchexternalButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent evt) {
				handleBinaryBrowseButtonSelected();
				updateLaunchConfigurationDialog();
			}
		});
	
		fLaunchernalButton = new Button(compnSIM,SWT.CHECK); //$NON-NLS-1$ //6-3
		fLaunchernalButton.setSelection(true);
		gd = new GridData(SWT.BEGINNING);
		fLaunchernalButton.setLayoutData(gd);
		fLaunchernalButton.setText("Launch nSIM");
		fLaunchernalButton.addSelectionListener(new SelectionListener() {

	        public void widgetSelected(SelectionEvent event) {

	        	if(fLaunchernalButton.getSelection()==true){
	        	fLaunchexternal_nsim_Buttonboolean="true";
	        	fSearchexternalButton.setEnabled(true);
	        	fSearchexternalLabel.setEnabled(true);
	        	fPrgmArgumentsTextexternal.setEnabled(true);
	        	}
	        	else {
	        		fLaunchexternal_nsim_Buttonboolean="false";
		        	fSearchexternalButton.setEnabled(false);
		        	fSearchexternalLabel.setEnabled(false);
		        	fPrgmArgumentsTextexternal.setEnabled(false);
	           	}
	        	updateLaunchConfigurationDialog();
	        }

	        public void widgetDefaultSelected(SelectionEvent event) {
	        }
	        
	      });
		
		
		nSIMtcflabel = new Label(compnSIM, SWT.CENTER);
		nSIMtcflabel.setText("nSIM TCF:");
	
		fnSIMtcfText = new Text(compnSIM, SWT.SINGLE | SWT.BORDER| SWT.BEGINNING);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint=400;
        gd.horizontalSpan=2;
        fnSIMtcfText.setLayoutData(gd);
        
	    if(nSIMtcffiles_last.equalsIgnoreCase(""))
	    	fnSIMtcfText.setText(nSIMtcffiles);
	    else 
	    	fnSIMtcfText.setText(nSIMtcffiles_last);
	    fnSIMtcfText.addModifyListener( new ModifyListener() {

			public void modifyText( ModifyEvent evt ) {
				nSIMtcffiles_last=fnSIMtcfText.getText();
				updateLaunchConfigurationDialog();
			}
		} );
		
	    fnSIMtcfButton = createPushButton(compnSIM, "Browse", null); //$NON-NLS-1$  //6-2
	    gd = new GridData(SWT.BEGINNING);
        gd.horizontalSpan=2;
        fnSIMtcfButton.setLayoutData(gd);
	
	    fnSIMtcfButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent evt) {
				handletcfBinaryBrowseButtonSelected();
				updateLaunchConfigurationDialog();
			}
		});
	    
		nSIMpropslabel = new Label(compnSIM, SWT.CENTER);
		nSIMpropslabel.setText("nSIM Props:");
	
		fnSIMpropsText = new Text(compnSIM, SWT.SINGLE | SWT.BORDER| SWT.BEGINNING);
		gd = new GridData(GridData.FILL_HORIZONTAL);
		gd.widthHint=400;
        gd.horizontalSpan=2;
        fnSIMpropsText.setLayoutData(gd);
	    if(nSIMpropsfiles_last.equalsIgnoreCase(""))
		    fnSIMpropsText.setText(nSIMpropsfiles);
	    else 
	    	fnSIMpropsText.setText(nSIMpropsfiles_last);
		fnSIMpropsText.addModifyListener( new ModifyListener() {

			public void modifyText( ModifyEvent evt ) {
				nSIMpropsfiles_last=fnSIMpropsText.getText();
				updateLaunchConfigurationDialog();
			}
		} );
		
		fnSIMpropslButton = createPushButton(compnSIM, "Browse", null); //$NON-NLS-1$  //6-2
		gd = new GridData(SWT.BEGINNING);
        gd.horizontalSpan=2;
        fnSIMpropslButton.setLayoutData(gd);
		fnSIMpropslButton.addSelectionListener(new SelectionAdapter() {
			public void widgetSelected(SelectionEvent evt) {
				handlepropsBinaryBrowseButtonSelected();
				updateLaunchConfigurationDialog();
			}
		});

		} 
	/* (non-Javadoc)
	 * @see org.eclipse.cdt.debug.mi.internal.ui.GDBDebuggerPage#createTabs(org.eclipse.swt.widgets.TabFolder)
	 */
	@Override
	public void createTabs( TabFolder tabFolder ) {
		super.createTabs( tabFolder );
		createGdbserverSettingsTab( tabFolder );
	}
	protected void handleBinaryBrowseButtonSelected() {
		if(fPrgmArgumentsComboInittext.equalsIgnoreCase("JTAG via OpenOCD")||fPrgmArgumentsComboInittext.equalsIgnoreCase("nSIM"))
		{
			FileDialog fileDialog = new FileDialog(getShell(), SWT.NONE);
			fileDialog.setFileName(fPrgmArgumentsTextexternal.getText());
			String text= fileDialog.open();
			if (text != null) {
				fPrgmArgumentsTextexternal.setText(text);
			}
			 
		}
		else if(fPrgmArgumentsComboInittext.equalsIgnoreCase("JTAG via Ashling"))
		{
			DirectoryDialog directoryDialog = new DirectoryDialog(getShell(), SWT.NONE);
			directoryDialog.setFilterPath(fPrgmArgumentsTextexternal.getText());
			String text= directoryDialog.open();
			if (text != null) {
				fPrgmArgumentsTextexternal.setText(text);
			}
			 
		}
	}
	
	protected void handlepropsBinaryBrowseButtonSelected() {
			FileDialog fileDialog = new FileDialog(getShell(), SWT.NONE);
			fileDialog.setFileName(fnSIMpropsText.getText());
			String text= fileDialog.open();
			if (text != null) {
				fnSIMpropsText.setText(text);
			}
	}
	protected void handletcfBinaryBrowseButtonSelected() {
		FileDialog fileDialog = new FileDialog(getShell(), SWT.NONE);
		fileDialog.setFileName(fnSIMtcfText.getText());
		String text= fileDialog.open();
		if (text != null) {
			fnSIMtcfText.setText(text);
		}
	}
	public static String getAttributeValueFromString(String string) {
		String content = string;
		if (content.length() > 0) {
			return content;
		}
		return null;
	}
}

