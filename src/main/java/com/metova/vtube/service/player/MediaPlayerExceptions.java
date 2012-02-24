/**
 * Copyright (c) 2009-2012 Martin M Reed, Metova Inc
 * 
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.metova.vtube.service.player;

import org.metova.bb.widgets.media.player.listener.RtspMediaPlayerListener;

import com.metova.vtube.model.player.MediaPlayerException;

public class MediaPlayerExceptions {

    private static final String CODE_1 = "1";
    private static final String MESSAGE_1 = "Media player busy: the media player is currently performing an operation that precludes the requested operation.";
    private static final String SOLUTION_1 = "Please try again. If the problem continues, try closing other running applications that may be playing media.";

    private static final String CODE_2 = "2";
    private static final String MESSAGE_2 = "Invalid parameter: a parameter was specified with an invalid value.";
    private static final String SOLUTION_2 = "Please try again."; // this shouldn't happen

    private static final String CODE_3 = "3";
    private static final String MESSAGE_3 = "Insufficient memory: there is insufficient memory to perform the requested operation.";
    private static final String SOLUTION_3 = "Please exit and restart the application. If the problem continues, try rebooting your device.";

    private static final String CODE_4 = "4";
    private static final String MESSAGE_4 = "Need more data: playback cannot proceed until the streaming source provides more data.";
    private static final String SOLUTION_4 = "Please try again. If the problem continues, there may be an issue with the server providing the video.";

    private static final String CODE_5 = "5";
    private static final String MESSAGE_5 = "Unspecified: some error occured which does not fit into any other category.";
    private static final String SOLUTION_5 = "Please try again. If the problem continues, you may not be able to play this video.";

    // this is for 4.6.0.167 users... 
    private static final String CODE_5bp = RtspMediaPlayerListener.BUFFER_PANIC_ERROR;
    private static final String MESSAGE_5bp = "Unstable buffering.";
    private static final String SOLUTION_5bp = "Please try again. If the problem continues, reducing the video quality may prevent further playback errors. This can be changed through the preferences screen.";

    public static final String CODE_5ml = "5 [ml]";
    private static final String MESSAGE_5ml = "Unable to initialize the media player.";
    private static final String SOLUTION_5ml = "Please try again. If the problem continues, please check that there are no application permissions set to \"Deny\".";

    private static final String CODE_6 = "6";
    private static final String MESSAGE_6 = "Format: an error in the media file was detected.";
    private static final String SOLUTION_6 = SOLUTION_4;

    private static final String CODE_7 = "7";
    private static final String MESSAGE_7 = "No server response: a server has stopped responding.";
    private static final String SOLUTION_7 = SOLUTION_4;

    private static final String CODE_8 = "8";
    private static final String MESSAGE_8 = "Connection lost: the current connection has been dropped.";
    private static final String SOLUTION_8 = "You may have insufficient network coverage to watch streaming video at this time. Please try again later.";

    private static final String CODE_9 = "9";
    private static final String MESSAGE_9 = "DNS resolve error: an invalid URL has been detected.";
    private static final String SOLUTION_9 = SOLUTION_8;

    private static final String CODE_10 = "10";
    private static final String MESSAGE_10 = "Unseekable: the media player needs to seek in the file in order to access headers, but can't since the file being played is unseekable.";
    private static final String SOLUTION_10 = SOLUTION_4; // this shouldn't happen

    private static final String CODE_11 = "11";
    private static final String MESSAGE_11 = "Connection timeout: the (streaming) server is unreachable.";
    private static final String SOLUTION_11 = SOLUTION_8;

    private static final String CODE_12 = "12";
    private static final String MESSAGE_12 = "No rights: indicates the DRM agent wasn't able to find a valid digital right in the media.";
    private static final String SOLUTION_12 = SOLUTION_4;

    private static final String CODE_13 = "13";
    private static final String MESSAGE_13 = "General client error: the streaming server rejected the streaming request.";
    private static final String SOLUTION_13 = SOLUTION_4;

    private static final String CODE_14 = "14";
    private static final String MESSAGE_14 = "Server error: an error occured on the streaming server while streaming.";
    private static final String SOLUTION_14 = SOLUTION_4;

    private static final String CODE_15 = "15";
    private static final String MESSAGE_15 = "Payment required: payment is required to stream this item from the server.";
    private static final String SOLUTION_15 = SOLUTION_4; // this shouldn't happen

    private static final String CODE_16 = "16";
    private static final String MESSAGE_16 = "Forbidden: the streaming server has rejected the streaming request for security reasons.";
    private static final String SOLUTION_16 = SOLUTION_4; // this shouldn't happen

    private static final String CODE_17 = "17";
    private static final String MESSAGE_17 = "Client file not found: the item required to stream doesn't exist or has been removed from the server.";
    private static final String SOLUTION_17 = SOLUTION_4; // this shouldn't happen

    private static final String CODE_18 = "18";
    private static final String MESSAGE_18 = "Client proxy authentication required: device needs to authenticate with a proxy server before streaming.";
    private static final String SOLUTION_18 = SOLUTION_4; // this shouldn't happen

    private static final String CODE_19 = "19";
    private static final String MESSAGE_19 = "Client request URI too large: the request URI sent to the server is too large.";
    private static final String SOLUTION_19 = SOLUTION_4; // this shouldn't happen

    private static final String CODE_20 = "20";
    private static final String MESSAGE_20 = "Not enough bandwidth: there is not enough bandwidth to support streaming.";
    private static final String SOLUTION_20 = SOLUTION_8;

    private static final String CODE_21 = "21";
    private static final String MESSAGE_21 = "Client session not found: streaming session has been removed by the server (e.g.: when paused for too long).";
    private static final String SOLUTION_21 = SOLUTION_4;

    private static final String CODE_22 = "22";
    private static final String MESSAGE_22 = "Unsupported transport: the streaming server/network doesn't support UDP/TCP streaming.";
    private static final String SOLUTION_22 = SOLUTION_8;

    public static final MediaPlayerException generateException( String code ) {

        // more common
        if ( CODE_9.equals( code ) ) {

            return new MediaPlayerException( CODE_9, MESSAGE_9, SOLUTION_9 );
        }
        else if ( CODE_5bp.equals( code ) ) {

            return new MediaPlayerException( CODE_5bp, MESSAGE_5bp, SOLUTION_5bp );
        }
        else if ( CODE_5ml.equals( code ) ) {

            return new MediaPlayerException( CODE_5ml, MESSAGE_5ml, SOLUTION_5ml );
        }
        else if ( CODE_5.equals( code ) ) {

            return new MediaPlayerException( CODE_5, MESSAGE_5, SOLUTION_5 );
        }

        // less common
        else if ( CODE_1.equals( code ) ) {

            return new MediaPlayerException( CODE_1, MESSAGE_1, SOLUTION_1 );
        }
        else if ( CODE_2.equals( code ) ) {

            return new MediaPlayerException( CODE_2, MESSAGE_2, SOLUTION_2 );
        }
        else if ( CODE_3.equals( code ) ) {

            return new MediaPlayerException( CODE_3, MESSAGE_3, SOLUTION_3 );
        }
        else if ( CODE_4.equals( code ) ) {

            return new MediaPlayerException( CODE_4, MESSAGE_4, SOLUTION_4 );
        }
        else if ( CODE_6.equals( code ) ) {

            return new MediaPlayerException( CODE_6, MESSAGE_6, SOLUTION_6 );
        }
        else if ( CODE_7.equals( code ) ) {

            return new MediaPlayerException( CODE_7, MESSAGE_7, SOLUTION_7 );
        }
        else if ( CODE_8.equals( code ) ) {

            return new MediaPlayerException( CODE_8, MESSAGE_8, SOLUTION_8 );
        }
        else if ( CODE_10.equals( code ) ) {

            return new MediaPlayerException( CODE_10, MESSAGE_10, SOLUTION_10 );
        }
        else if ( CODE_11.equals( code ) ) {

            return new MediaPlayerException( CODE_11, MESSAGE_11, SOLUTION_11 );
        }
        else if ( CODE_12.equals( code ) ) {

            return new MediaPlayerException( CODE_12, MESSAGE_12, SOLUTION_12 );
        }
        else if ( CODE_13.equals( code ) ) {

            return new MediaPlayerException( CODE_13, MESSAGE_13, SOLUTION_13 );
        }
        else if ( CODE_14.equals( code ) ) {

            return new MediaPlayerException( CODE_14, MESSAGE_14, SOLUTION_14 );
        }
        else if ( CODE_15.equals( code ) ) {

            return new MediaPlayerException( CODE_15, MESSAGE_15, SOLUTION_15 );
        }
        else if ( CODE_16.equals( code ) ) {

            return new MediaPlayerException( CODE_16, MESSAGE_16, SOLUTION_16 );
        }
        else if ( CODE_17.equals( code ) ) {

            return new MediaPlayerException( CODE_17, MESSAGE_17, SOLUTION_17 );
        }
        else if ( CODE_18.equals( code ) ) {

            return new MediaPlayerException( CODE_18, MESSAGE_18, SOLUTION_18 );
        }
        else if ( CODE_19.equals( code ) ) {

            return new MediaPlayerException( CODE_19, MESSAGE_19, SOLUTION_19 );
        }
        else if ( CODE_20.equals( code ) ) {

            return new MediaPlayerException( CODE_20, MESSAGE_20, SOLUTION_20 );
        }
        else if ( CODE_21.equals( code ) ) {

            return new MediaPlayerException( CODE_21, MESSAGE_21, SOLUTION_21 );
        }
        else if ( CODE_22.equals( code ) ) {

            return new MediaPlayerException( CODE_22, MESSAGE_22, SOLUTION_22 );
        }

        return null;
    }
}
