package org.iif.smart_demo.mappers

import org.iif.smart_proxy.domain.StreamData
import org.iif.smart_proxy.domain.StreamType
import org.iif.smart_demo.domain.Stream

fun Stream.toStreamData() = StreamData(hlsVideoSrc, StreamType.HLS)