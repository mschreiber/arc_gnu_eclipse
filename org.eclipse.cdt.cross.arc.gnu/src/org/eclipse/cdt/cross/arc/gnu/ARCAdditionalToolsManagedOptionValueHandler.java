 package org.eclipse.cdt.cross.arc.gnu;
 
 /*******************************************************************************
 * This program and the accompanying materials 
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 * 
 * Contributors:
 *     Synopsys, Inc. - ARC GNU Toolchain support
 *******************************************************************************/

 
 import java.io.PrintStream;
 import org.eclipse.cdt.managedbuilder.core.IBuildObject;
 import org.eclipse.cdt.managedbuilder.core.IHoldsOptions;
 import org.eclipse.cdt.managedbuilder.core.IOption;
 import org.eclipse.cdt.managedbuilder.core.ManagedOptionValueHandler;
 import org.eclipse.cdt.managedbuilder.internal.core.FolderInfo;
 import org.eclipse.cdt.managedbuilder.internal.core.ResourceConfiguration;
 
 public class ARCAdditionalToolsManagedOptionValueHandler extends ManagedOptionValueHandler
 {
   public boolean handleValue(IBuildObject configuration, IHoldsOptions holder, IOption option, String extraArgument, int event)
   {
     if (event == 4) {
  
       if ((configuration instanceof FolderInfo))
       {
         FolderInfo oFolderInfo = (FolderInfo)configuration;
 
         return true;
       }if (!(configuration instanceof ResourceConfiguration))
       {
         System.out.println("unexpected instanceof configuration " + configuration.getClass().getCanonicalName());
       }
 
     }
 
     return false;
   }
 }
