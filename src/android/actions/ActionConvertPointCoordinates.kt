/*
 * This file is part of the Scandit Data Capture SDK
 *
 * Copyright (C) 2019- Scandit AG. All rights reserved.
 */

package com.scandit.datacapture.cordova.core.actions

import com.scandit.datacapture.cordova.core.data.SerializablePoint
import com.scandit.datacapture.cordova.core.testing.OpenForTesting
import com.scandit.datacapture.core.common.geometry.Point
import com.scandit.datacapture.core.ui.DataCaptureView
import org.apache.cordova.CallbackContext
import org.json.JSONArray

@OpenForTesting
class ActionConvertPointCoordinates(
    private val dataCaptureView: DataCaptureView?,
    private val listener: ResultListener
) : Action {

    override fun run(args: JSONArray, callbackContext: CallbackContext) {
        try {
            if (dataCaptureView == null) {
                listener.onConvertPointCoordinatesNoViewError(callbackContext)
            } else {
                val point = SerializablePoint(args.getJSONObject(0)).toScanditPoint()
                listener.onConvertPointCoordinates(
                    dataCaptureView.mapFramePointToView(point),
                    callbackContext
                )
            }
        } catch (e: Exception) {// TODO SDC-1851 fine-catch deserializer exceptions
            e.printStackTrace()
            listener.onJsonParseError(e, callbackContext)
        }
    }

    interface ResultListener : ActionJsonParseErrorResultListener {
        fun onConvertPointCoordinates(point: Point, callbackContext: CallbackContext)
        fun onConvertPointCoordinatesNoViewError(callbackContext: CallbackContext)
    }
}
