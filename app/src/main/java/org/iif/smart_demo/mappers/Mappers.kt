package org.iif.smart_demo.mappers

import org.iif.smartproxy.domain.StreamData
import org.iif.smartproxy.domain.StreamType
import org.iif.smart_demo.domain.Stream

fun Stream.toStreamData() = StreamData(hlsVideoSrc, StreamType.HLS)