/*
 * Copyright 2023 Ant Group
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.alipay.antchain.bridge.commons;

import java.io.*;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.Security;
import java.security.Signature;
import java.util.Date;

import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.KeyUtil;
import cn.hutool.crypto.PemUtil;
import cn.hutool.crypto.digest.SM3;
import com.alipay.antchain.bridge.commons.bcdns.*;
import com.alipay.antchain.bridge.commons.bcdns.utils.CrossChainCertificateUtil;
import com.alipay.antchain.bridge.commons.core.base.CrossChainDomain;
import com.alipay.antchain.bridge.commons.core.base.ObjectIdentity;
import com.alipay.antchain.bridge.commons.core.base.ObjectIdentityType;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.jcajce.JcaPEMWriter;
import org.junit.Assert;
import org.junit.Test;

public class BCDNSTest {

    @Test
    public void testCrossChainCertificate() throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        KeyPair keyPair = KeyUtil.generateKeyPair("SM2");
        keyPair.getPublic().getEncoded();

        // construct a bcdns root cert
        AbstractCrossChainCertificate certificate = CrossChainCertificateFactory.createCrossChainCertificate(
                CrossChainCertificateV1.MY_VERSION,
                "test",
                new ObjectIdentity(ObjectIdentityType.X509_PUBLIC_KEY_INFO, keyPair.getPublic().getEncoded()),
                DateUtil.currentSeconds(),
                DateUtil.offsetDay(new Date(), 365).second(),
                new BCDNSTrustRootCredentialSubject(
                        "bif",
                        new ObjectIdentity(ObjectIdentityType.X509_PUBLIC_KEY_INFO, keyPair.getPublic().getEncoded()),
                        new byte[]{}
                )
        );

        // this is how to sign something with private key
        Signature signer = Signature.getInstance("SM3WITHSM2");
        signer.initSign(keyPair.getPrivate());
        signer.update(certificate.getEncodedToSign());
        byte[] signature = signer.sign();

        // this is how to verify the signature
        Signature verifier = Signature.getInstance("SM3WITHSM2");
        verifier.initVerify(keyPair.getPublic());
        verifier.update(certificate.getEncodedToSign());
        Assert.assertTrue(verifier.verify(signature));

        certificate.setProof(
                new AbstractCrossChainCertificate.IssueProof(
                        "SM3",
                        SM3.create().digest(certificate.getEncodedToSign()),
                        "SM3WITHSM2",
                        signature
                )
        );
        System.out.println(CrossChainCertificateUtil.formatCrossChainCertificateToPem(certificate));

        // construct a domain cert
        AbstractCrossChainCertificate domainCert = CrossChainCertificateFactory.createCrossChainCertificate(
                CrossChainCertificateV1.MY_VERSION,
                "testdomain",
                new ObjectIdentity(ObjectIdentityType.X509_PUBLIC_KEY_INFO, keyPair.getPublic().getEncoded()),
                DateUtil.currentSeconds(),
                DateUtil.offsetDay(new Date(), 365).second(),
                new DomainNameCredentialSubject(
                        DomainNameCredentialSubject.CURRENT_VERSION,
                        DomainNameTypeEnum.DOMAIN_NAME,
                        new CrossChainDomain("antchain.com"),
                        new ObjectIdentity(ObjectIdentityType.X509_PUBLIC_KEY_INFO, keyPair.getPublic().getEncoded()),
                        new byte[]{}
                )
        );

        signer = Signature.getInstance("SM3WITHSM2");
        signer.initSign(keyPair.getPrivate());
        signer.update(domainCert.getEncodedToSign());
        domainCert.setProof(
                new AbstractCrossChainCertificate.IssueProof(
                        "SM3",
                        SM3.create().digest(domainCert.getEncodedToSign()),
                        "SM3WITHSM2",
                        signer.sign()
                )
        );
        System.out.println(CrossChainCertificateUtil.formatCrossChainCertificateToPem(domainCert));

        // construct a domain space cert
        AbstractCrossChainCertificate domainSpaceCert = CrossChainCertificateFactory.createCrossChainCertificate(
                CrossChainCertificateV1.MY_VERSION,
                ".com",
                new ObjectIdentity(ObjectIdentityType.X509_PUBLIC_KEY_INFO, keyPair.getPublic().getEncoded()),
                DateUtil.currentSeconds(),
                DateUtil.offsetDay(new Date(), 365).second(),
                new DomainNameCredentialSubject(
                        DomainNameCredentialSubject.CURRENT_VERSION,
                        DomainNameTypeEnum.DOMAIN_NAME_SPACE,
                        new CrossChainDomain(".com"),
                        new ObjectIdentity(ObjectIdentityType.X509_PUBLIC_KEY_INFO, keyPair.getPublic().getEncoded()),
                        new byte[]{}
                )
        );
        signer = Signature.getInstance("SM3WITHSM2");
        signer.initSign(keyPair.getPrivate());
        signer.update(domainSpaceCert.getEncodedToSign());
        domainSpaceCert.setProof(
                new AbstractCrossChainCertificate.IssueProof(
                        "SM3",
                        SM3.create().digest(domainSpaceCert.getEncodedToSign()),
                        "SM3WITHSM2",
                        signer.sign()
                )
        );
        System.out.println(CrossChainCertificateUtil.formatCrossChainCertificateToPem(domainSpaceCert));

        // construct a relayer cert
        AbstractCrossChainCertificate relayerCert = CrossChainCertificateFactory.createCrossChainCertificate(
                CrossChainCertificateV1.MY_VERSION,
                "antchain-relayer",
                new ObjectIdentity(ObjectIdentityType.X509_PUBLIC_KEY_INFO, keyPair.getPublic().getEncoded()),
                DateUtil.currentSeconds(),
                DateUtil.offsetDay(new Date(), 365).second(),
                new RelayerCredentialSubject(
                        RelayerCredentialSubject.CURRENT_VERSION,
                        "antchain-relayer",
                        new ObjectIdentity(ObjectIdentityType.X509_PUBLIC_KEY_INFO, keyPair.getPublic().getEncoded()),
                        new byte[]{}
                )
        );
        signer = Signature.getInstance("SM3WITHSM2");
        signer.initSign(keyPair.getPrivate());
        signer.update(relayerCert.getEncodedToSign());
        relayerCert.setProof(
                new AbstractCrossChainCertificate.IssueProof(
                        "SM3",
                        SM3.create().digest(relayerCert.getEncodedToSign()),
                        "SM3WITHSM2",
                        signer.sign()
                )
        );
        System.out.println(CrossChainCertificateUtil.formatCrossChainCertificateToPem(relayerCert));

        // dump the private key into pem
        StringWriter stringWriter = new StringWriter(256);
        JcaPEMWriter jcaPEMWriter = new JcaPEMWriter(stringWriter);
        jcaPEMWriter.writeObject(keyPair.getPrivate());
        jcaPEMWriter.close();
        String privatePem = stringWriter.toString();
        System.out.println(privatePem);

        PrivateKey privateKey = PemUtil.readPemPrivateKey(new ByteArrayInputStream(privatePem.getBytes()));
        Assert.assertNotNull(privatePem);
    }
}
