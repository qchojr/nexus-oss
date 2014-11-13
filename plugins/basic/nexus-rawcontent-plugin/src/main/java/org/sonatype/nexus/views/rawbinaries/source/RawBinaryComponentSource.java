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
package org.sonatype.nexus.views.rawbinaries.source;

import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Nullable;

import org.sonatype.nexus.component.model.Asset;
import org.sonatype.nexus.component.model.Component;
import org.sonatype.nexus.component.model.ComponentEnvelope;
import org.sonatype.nexus.component.source.ComponentRequest;
import org.sonatype.nexus.component.source.ComponentSource;
import org.sonatype.nexus.component.source.ComponentSourceId;
import org.sonatype.nexus.component.source.support.ExtraCloseableStream;
import org.sonatype.nexus.views.rawbinaries.internal.RawAsset;
import org.sonatype.nexus.views.rawbinaries.internal.RawComponent;

import com.google.common.base.Supplier;
import com.google.common.base.Throwables;
import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Arrays.asList;

/**
 * A source for remote {@link RawComponent}s which provides HTTP resources under a given URL prefix as assets.
 *
 * @since 3.0
 */
public abstract class RawBinaryComponentSource
    implements ComponentSource
{
  private final ComponentSourceId sourceName;

  private final String urlPrefix;

  private CloseableHttpClient httpClient;

  public RawBinaryComponentSource(final ComponentSourceId sourceName,
                                  final CloseableHttpClient httpClient,
                                  final String urlPrefix)
  {
    this.sourceName = checkNotNull(sourceName);
    this.urlPrefix = checkNotNull(urlPrefix);
    this.httpClient = checkNotNull(httpClient);
  }

   @Nullable
  //@Override
  @SuppressWarnings("unchecked")
  // TODO: The method is parameterized, but we're assuming specific types in the implementation.
  //       Consider either changing the *class* (interface) to be parameterized instead of the method,
  //       or making the construction of Component and Asset subclasses in the impl less presumptious.
  public <C extends Component, A extends Asset> Iterable<ComponentEnvelope<C, A>> fetchComponents2(
      final ComponentRequest<C, A> request) throws IOException
  {
    final String uri = urlPrefix + request.getQuery().get("path");

    final HttpGet httpGet = new HttpGet(uri);

    final CloseableHttpResponse response = httpClient.execute(httpGet);

    A asset = (A) new RawAsset();
    final HttpEntity httpEntity = response.getEntity();
    final Header contentType = httpEntity.getContentType();
    if (contentType != null) {
      asset.setContentType(contentType.getValue());
    }
    asset.setContentLength(0);
    asset.setStreamSupplier(new Supplier<InputStream>()
    {
      @Override
      public InputStream get() {
        try {
          return new ExtraCloseableStream(httpEntity.getContent(), response);
        }
        catch (IOException e) {
          throw Throwables.propagate(e);
        }
      }
    });

    return asList(ComponentEnvelope.simpleEnvelope((C) new RawComponent(), asset));
  }

  @Override
  public ComponentSourceId getId() {
    return sourceName;
  }
}
