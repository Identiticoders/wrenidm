/*
 * The contents of this file are subject to the terms of the Common Development and
 * Distribution License (the License). You may not use this file except in compliance with the
 * License.
 *
 * You can obtain a copy of the License at legal/CDDLv1.0.txt. See the License for the
 * specific language governing permission and limitations under the License.
 *
 * When distributing Covered Software, include this CDDL Header Notice in each file and include
 * the License file at legal/CDDLv1.0.txt. If applicable, add the following below the CDDL
 * Header, with the fields enclosed by brackets [] replaced by your own identifying
 * information: "Portions copyright [year] [name of copyright owner]".
 *
 * Copyright 2016 ForgeRock AS.
 * Portions Copyright 2018 Wren Security.
 */

package org.forgerock.openidm.keystore.impl;

import static org.assertj.core.api.Assertions.assertThat;
import static org.forgerock.openidm.core.IdentityServer.HTTPS_KEYSTORE_CERT_ALIAS;
import static org.forgerock.openidm.core.IdentityServer.KEYSTORE_LOCATION;
import static org.forgerock.openidm.core.IdentityServer.KEYSTORE_PASSWORD;
import static org.forgerock.openidm.core.IdentityServer.KEYSTORE_TYPE;
import static org.forgerock.openidm.core.IdentityServer.TRUSTSTORE_LOCATION;
import static org.forgerock.openidm.core.IdentityServer.TRUSTSTORE_PASSWORD;
import static org.forgerock.openidm.core.IdentityServer.TRUSTSTORE_TYPE;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.security.KeyStore;
import java.security.Security;
import java.util.Collections;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.forgerock.openidm.core.IdentityServer;
import org.forgerock.openidm.core.IdentityServerTestUtils;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class TrustStoreServiceImplTest {
    @BeforeClass
    public void setUp() throws Exception {
        Security.addProvider(new BouncyCastleProvider());
        IdentityServerTestUtils.initInstanceForTest(this.getClass());
    }

    @Test
    public void testKeyStoreServiceCreation() throws Exception {
        // given
        createKeyStores();
        final TrustStoreServiceImpl trustStoreService = new TrustStoreServiceImpl();
        final KeyStoreServiceImpl keyStoreService = new KeyStoreServiceImpl();
        keyStoreService.activate(null);
        trustStoreService.bindKeyStore(keyStoreService);

        // when
        trustStoreService.activate(null);

        // then
        assertThat(trustStoreService.getKeyStore()).isNotNull();
        assertThat(Collections.list(trustStoreService.getKeyStore().aliases()))
                .asList()
                .hasSize(1)
                .contains(IdentityServer.getInstance().getProperty(HTTPS_KEYSTORE_CERT_ALIAS, "openidm-localhost"));
        assertThat(trustStoreService.getKeyStoreDetails()).isNotNull();
    }

    private void createKeyStores() throws Exception {
        createKeyStore(IdentityServer.getFileForPath(IdentityServer.getInstance().getProperty(KEYSTORE_LOCATION)));
        createTrustStore(IdentityServer.getFileForPath(IdentityServer.getInstance().getProperty(TRUSTSTORE_LOCATION)));
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void createKeyStore(final File keystoreFile) throws Exception {
        keystoreFile.deleteOnExit();
        if (keystoreFile.exists()) {
            keystoreFile.delete();
        }

        keystoreFile.getParentFile().mkdirs();
        assertThat(keystoreFile.createNewFile()).isTrue().as("Unable to create keystore file");
        try (final OutputStream outputStream = new FileOutputStream(keystoreFile)) {
            final KeyStore keyStore =
                    KeyStore.getInstance(IdentityServer.getInstance().getProperty(KEYSTORE_TYPE));
            keyStore.load(null, IdentityServer.getInstance().getProperty(KEYSTORE_PASSWORD).toCharArray());
            keyStore.store(
                    outputStream,
                    IdentityServer.getInstance().getProperty(KEYSTORE_PASSWORD).toCharArray()
            );
        }
    }

    @SuppressWarnings("ResultOfMethodCallIgnored")
    private void createTrustStore(final File keystoreFile) throws Exception {
        keystoreFile.deleteOnExit();
        if (keystoreFile.exists()) {
            keystoreFile.delete();
        }

        keystoreFile.getParentFile().mkdirs();
        assertThat(keystoreFile.createNewFile()).isTrue().as("Unable to create keystore file");
        try (final OutputStream outputStream = new FileOutputStream(keystoreFile)) {
            final KeyStore keyStore =
                    KeyStore.getInstance(IdentityServer.getInstance().getProperty(TRUSTSTORE_TYPE));
            keyStore.load(null, IdentityServer.getInstance().getProperty(TRUSTSTORE_PASSWORD).toCharArray());
            keyStore.store(
                    outputStream,
                    IdentityServer.getInstance().getProperty(TRUSTSTORE_PASSWORD).toCharArray()
            );
        }
    }
}
