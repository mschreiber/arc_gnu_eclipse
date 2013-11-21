/*******************************************************************************
* This program and the accompanying materials 
* are made available under the terms of the Common Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/cpl-v10.html
* 
* Contributors:
*     Synopsys, Inc. - ARC GNU Toolchain support
*******************************************************************************/

package org.eclipse.cdt.cross.arc.gnu.common;
 
 public class IsToolchainData
 {
   public boolean m_bSuppChecked;
   public boolean m_bToolchainIsSupported;
   public String m_sBinPath;
 
   public IsToolchainData()
   {
     this.m_bSuppChecked = false;
     this.m_bToolchainIsSupported = false;
     this.m_sBinPath = null;
   }
 }
