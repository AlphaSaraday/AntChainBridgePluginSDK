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

package com.alipay.antchain.bridge.commons.exception;

import lombok.Getter;

/**
 * Error code for {@code antchain-bridge-commons}
 *
 * <p>
 *     The {@code errorCode} field supposed to be hex and has two bytes.
 *     First byte represents the space code for project.
 *     Last byte represents the specific error scenarios.
 * </p>
 *
 * <p>
 *     Space code interval for {@code antchain-bridge-commons} is from 00 to 3f.
 * </p>
 */
@Getter
public enum CommonsErrorCodeEnum {
    /**
     * The type of {@link com.alipay.antchain.bridge.commons.core.base.CrossChainMessage} is not recognized
     */
    UNSUPPORTED_CROSS_CHAIN_MESSAGE_TYPE_ERROR("0001", "unsupported cc msg type"),

    /**
     * The type of {@link com.alipay.antchain.bridge.commons.core.base.ObjectIdentity} is not recognized
     */
    UNSUPPORTED_OID_TYPE_ERROR("0002", "unsupported oid type"),

    /**
     * Something wrong about {@code AuthMessage}, like version, etc.
     */
    INCORRECT_AUTH_MESSAGE_ERROR("0101", "wrong am"),

    /**
     * Code shows where decode {@code AuthMessage} failed
     */
    AUTH_MESSAGE_DECODE_ERROR("0102", "am decode failed"),

    /**
     * Code shows where decode {@code SDPMessage} failed
     */
    INCORRECT_SDP_MESSAGE_ERROR("0201", "wrong sdp msg"),

    /**
     * Code shows where decode {@code SDPMessage} failed
     */
    SDP_MESSAGE_DECODE_ERROR("0202", "sdp decode failed"),

    /**
     * Code shows where decode {@code CrossChainIdentity} failed
     */
    CROSS_CHAIN_IDENTITY_DECODE_ERROR("0302", "ccid decode failed"),

    /**
     * Code shows where decode {@code SDPMessage} failed
     */
    RULES_CHECK_ERROR("0404", "antchain-bridge data check failed"),

    /**
     * Something wrong about {@code BlockchainTrustAnchor}, like version, etc.
     */
    INCORRECT_BTA("0504", "incorrect bta"),

    /**
     * Something wrong when encoding object to {@code TLV} packet.
     */
    CODEC_TLV_ENCODE_ERROR("0601", "wrong tlv"),

    /**
     * Something wrong when encoding object to {@code TLV} packet.
     */
    CODEC_TLV_DECODE_ERROR("0602", "wrong tlv"),

    /**
     * Unsupported basic TLV type
     */
    CODEC_TLV_UNSUPPORTED_TYPE("0603", "unsupported tlv type");

    /**
     * Error code for errors happened in project {@code antchain-bridge-commons}
     */
    private final String errorCode;

    /**
     * Every code has a short message to describe the error stuff
     */
    private final String shortMsg;

    CommonsErrorCodeEnum(String errorCode, String shortMsg) {
        this.errorCode = errorCode;
        this.shortMsg = shortMsg;
    }
}
