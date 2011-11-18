/**
 * Copyright (c) 2008-2011 Sonatype, Inc.
 * All rights reserved. Includes the third-party code listed at http://links.sonatype.com/products/nexus/oss/attributions
 *
 * This program is free software: you can redistribute it and/or modify it only under the terms of the GNU Affero General
 * Public License Version 3 as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Affero General Public License Version 3
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License Version 3 along with this program.  If not, see
 * http://www.gnu.org/licenses.
 *
 * Sonatype Nexus (TM) Open Source Version is available from Sonatype, Inc. Sonatype and Sonatype Nexus are trademarks of
 * Sonatype, Inc. Apache Maven is a trademark of the Apache Foundation. M2Eclipse is a trademark of the Eclipse Foundation.
 * All other trademarks are the property of their respective owners.
 */
package org.sonatype.nexus.plugins.capabilities.api.descriptor;

import java.util.List;
import java.util.Map;

import org.sonatype.nexus.formfields.FormField;
import org.sonatype.nexus.plugins.capabilities.api.CapabilityFactory;
import org.sonatype.plugin.ExtensionPoint;

/**
 * Describes a capability (its type).
 */
@ExtensionPoint
public interface CapabilityDescriptor
{

    /**
     * Returns the unique identifier of capability. This ID is used to related the descriptor to
     * {@link CapabilityFactory}.
     *
     * @return unique identifier of capability
     */
    String id();

    /**
     * Returns a user friendly name of capability (to be presented in UI).
     *
     * @return capability type name.
     */
    String name();

    /**
     * Returns capability form fields (properties).
     *
     * @return capability form fields (properties).
     */
    List<FormField> formFields();

    /**
     * Whether or not this capability is user facing = user should be able to configure it or it is a capability that
     * will be created via other means (e.g. created by some other Nexus functionality)
     *
     * @return if capability is user facing.
     */
    boolean isExposed();

    /**
     * Describes the capability based on capability configuration properties. The method is called in case that
     * capability name on creation/update of capability.
     *
     * @param properties capability configuration properties
     * @return capability description
     * @since 1.10.0
     */
    String describe( Map<String, String> properties );

}