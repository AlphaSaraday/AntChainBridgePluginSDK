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

package com.alipay.antchain.bridge.plugins.spi.bbc.core.read;

import java.util.List;

import com.alipay.antchain.bridge.commons.core.base.CrossChainMessage;
import com.alipay.antchain.bridge.commons.core.base.CrossChainMessageReceipt;

/**
 * We can read the antchain-bridge data from blockchain through
 * {@code IAntChainBridgeDataReader}.
 *
 * <p>
 *     AntChainBridge data is the data generated by the AntChainBridge
 *     system during the cross-chain process. For example,
 *     A transaction confirmed on blockchain invoke the contract to send
 *     a cross-chain message and the message could be stored
 *     into the transaction receipt. The message in the receipt
 *     is a kind of antchain-bridge data. And {@code IAntChainBridgeDataReader}
 *     needs to provide the method to catch the cross-chain
 *     message. Mostly, {@code IAntChainBridgeDataReader} instance fetch
 *     the antchain-bridge data by filter blocks or transactions with a
 *     specific event topic through blockchain SDK.
 * </p>
 *
 * <p>
 *     All types of antchain-bridge data such as
 *     {@link CrossChainMessage} and so on can be found in
 *     project {@code antchain-bridge-commons}.
 * </p>
 *
 * <p>
 *     All {@code IAntChainBridgeDataReader} functions are split into multiple
 *     interfaces like {@link ISDPReader}.
 * </p>
 */
public interface IAntChainBridgeDataReader extends ISDPReader, IVerifierReader {

    /**
     * Fetch the cross-chain message receipt that contains information about the transaction
     * execution result, if transaction confirmed by the blockchain and the error message stuff.
     *
     * <p>
     *     Mostly, this function would check if the transaction referred
     *     in {@code receipt} which is a instance for {@link CrossChainMessageReceipt}
     *     confirmed.
     * </p>
     *
     * @param txhash the transaction hash used to committing the auth-message
     * @return if {@link CrossChainMessage} confirmed
     */
    CrossChainMessageReceipt readCrossChainMessageReceipt(String txhash);

    /**
     * Get a list of cross-chain messages at a given height.
     *
     * <p>
     *     Someone calls the contracts on blockchain and it would leave some
     *     trace data on the ledger. The cross-chain system contracts would also
     *     produce some designed trace data like a type of event with special
     *     topics. We can catch the trace data with scanning the blocks.
     *     According to the mechanism of blockchain, the data on the ledger
     *     will always have a corresponding proof. The trace data and the proof
     *     make up the {@link CrossChainMessage.ProvableLedgerData}.
     * </p>
     *
     * <p>
     *     Contract {@code AuthMessage} is the contract who produce the trace
     *     data for cross-chain. And the raw cross-chain message called {@code AuthMessage}
     *     is stored in the trace data. The raw message can be placed in
     *     the field {@link CrossChainMessage#setMessage(byte[])}.
     * </p>
     *
     * <p>
     *     With {@code ProvableLedgerData} and the raw cross-chain message, you can
     *     create an instance of {@link CrossChainMessage}.
     * </p>
     *
     * <p>
     *     Mostly, a {@code IAntChainBridgeDataReader} scanning the block on {@code height} in parameters
     *     through the blockchain SDK to find the {@link CrossChainMessage}s.
     * </p>
     *
     * @param height the height to scan
     * @return list of {@link CrossChainMessage}
     */
    List<CrossChainMessage> readCrossChainMessagesByHeight(long height);

    /**
     * Method {@code queryLatestHeight} queries the latest height of this blockchain
     * which the {@code BBCService} object connected with.
     *
     * @return {@link Long}
     */
    Long queryLatestHeight();
}
