/*
 * Sonatype Nexus (TM) Open Source Version
 * Copyright (c) 2007-2014 Sonatype, Inc.
 * All rights reserved. Includes the third-party code listed at http://links.sonatype.com/products/nexus/oss/attributions.
 *
 * This program and the accompanying materials are made available under the terms of the Eclipse Public License Version 1.0,
 * which accompanies this distribution and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Sonatype Nexus (TM) Professional Version is available from Sonatype, Inc. "Sonatype" and "Sonatype Nexus" are trademarks
 * of Sonatype, Inc. Apache Maven is a trademark of the Apache Software Foundation. M2eclipse is a trademark of the
 * Eclipse Foundation. All other trademarks are the property of their respective owners.
 */
package org.sonatype.nexus.scheduling.events;

import org.sonatype.nexus.events.AbstractEvent;
import org.sonatype.nexus.scheduling.TaskInfo;

/**
 * Abstract super class for task related events.
 *
 * @since 2.0
 */
public abstract class NexusTaskEvent<T>
    extends AbstractEvent<TaskInfo<T>>
{
  public NexusTaskEvent(final TaskInfo<T> taskInfo) {
    super(taskInfo);
  }

  /**
   * Returns the task that failed.
   */
  public TaskInfo<T> getNexusTaskInfo() {
    return getEventSender();
  }

  @Override
  public String toString() {
    return getClass().getSimpleName() + "{" +
        "nexusTask=" + getNexusTaskInfo().getConfiguration() +
        '}';
  }
}
