/*
 * Copyright (c) 2017 Pantheon Technologies s.r.o. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.openflowplugin.impl.protocol.deserialization.multipart;

import static org.junit.Assert.assertEquals;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.UnpooledByteBufAllocator;
import org.junit.Test;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.table.statistics.rev131215.flow.table.and.statistics.map.FlowTableAndStatisticsMap;
import org.opendaylight.yang.gen.v1.urn.opendaylight.flow.table.statistics.rev131215.multipart.reply.multipart.reply.body.MultipartReplyFlowTableStats;
import org.opendaylight.yang.gen.v1.urn.opendaylight.openflow.common.types.rev130731.MultipartType;

public class MultipartReplyFlowTableStatsDeserializerTest extends AbstractMultipartDeserializerTest {
    private static final byte TABLE_ID = 2;
    private static final long PACKETS_LOOKEDUP = 1L;
    private static final long PACKETS_MATCHED = 2L;
    private static final int ACTIVE_FLOWS = 3;
    private static final byte PADDING_IN_TABLE_HEADER = 3;

    @Test
    public void testDeserialize() {
        ByteBuf buffer = UnpooledByteBufAllocator.DEFAULT.buffer();
        buffer.writeByte(TABLE_ID);
        buffer.writeZero(PADDING_IN_TABLE_HEADER);
        buffer.writeInt(ACTIVE_FLOWS);
        buffer.writeLong(PACKETS_LOOKEDUP);
        buffer.writeLong(PACKETS_MATCHED);

        final MultipartReplyFlowTableStats reply = (MultipartReplyFlowTableStats) deserializeMultipart(buffer);
        final FlowTableAndStatisticsMap first = reply.nonnullFlowTableAndStatisticsMap().values().iterator().next();
        assertEquals(TABLE_ID, first.getTableId().getValue().byteValue());
        assertEquals(ACTIVE_FLOWS, first.getActiveFlows().getValue().intValue());
        assertEquals(PACKETS_LOOKEDUP, first.getPacketsLookedUp().getValue().longValue());
        assertEquals(PACKETS_MATCHED, first.getPacketsMatched().getValue().longValue());
        assertEquals(0, buffer.readableBytes());
    }

    @Override
    protected int getType() {
        return MultipartType.OFPMPTABLE.getIntValue();
    }
}
