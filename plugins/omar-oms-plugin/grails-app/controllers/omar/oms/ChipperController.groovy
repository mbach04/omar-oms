package omar.oms

import omar.core.BindUtil
import omar.core.HttpStatus

import io.swagger.annotations.*

@Api( value = "/chipper",
      description = "API operations for Chipper"
)
class ChipperController {
   def chipperService
   def index()
   {
      def chipperParams = params - params.subMap( [ 'controller', 'format' ] )
      def json = request.JSON
      String operation = chipperParams.find { it.key.equalsIgnoreCase( 'operation' ) }?.value

      if(!operation && json)
      {
         operation = json.operation
      }

      switch ( operation?.toLowerCase() )
      {
      case "chip":
         forward action: 'chip'
         break
      case "ortho":
         forward action: 'ortho'
         break
      default:
         render ""
         break
      }
   }

  @ApiOperation(value = "Get image space chip from the passed in image file name",
                produces="image/png,image/jpeg,image/gif,image/tiff,text/plain",
                httpMethod="GET",
                notes = """
*   **images**

    Is an array of objects of the form images[0].file = **filename** and an optional images[0].entry = **entry**

*   **operation**

    Is fixed to the value 'chip'

*   **nullPixelFlip**

    Flips null interior pixels to valid

*   **brightness**

    Allows one to control the brightness of the image. This is expressed as a normalized value between -1 and 1.

*   **contrast**

    Allows one to control the contrast of an image. This is a multiplier.

*   **sharpenMode**

    Sharpen mode can take on the values none, light, or heavy.

*   **thumbnailResolution**

    Specify the resolution of the thumbnail. The valie is assumed square so only one dimension is needed. Setting the value to 512 will output to a 512x512 image.

*   **cutBboxXywh**

    This is used to cut in image space. It takes values **x**,**y**,**width**,**height** where x and y are defined as the upper left corner and the width extends positive x to the right and height extends positive y down.

*   **rrds**

    This is the reduced resolution dataset. Starts from 0 to number of res levels -1.

*   **histOp**

    Histogram operations used can be none, auto-minmax, auto-percentile, std-stretch-1, std-stretch-2, std-stretch-3

*   **histCenter**

    Histogram should be calculated based on the center of the request

*   **outputRadiometry**

    Defines the output radiometry.

*   **bands**

    This defines an output band list that is a comma separated value of integers with band index 1's based. So a value of 1,1,1 will output three bands with all being the first band of the input image. If you have a 3 band input image you can reverse them by doing 3,2,1. Commas are required.

*   **resamplerFilter**

    We have exposed different filtering capabilities. Values can be any of the following nearest-neighbor, bilinear, cubic, gaussian, blackman, bspline, hanning, hamming, hermite, mitchell, quadratic, sinc, magic

*   **outputFormat**

    Output format can be 'image/jpeg', 'image/png', 'image/gif', 'image/tiff'

*   **keepBands**

    This is a flag that will allow the bands to be kept as specified. So if you wnat a 10 band output then you must void setUp() the **keepBands** to true and then specify the **outputFormat** to be image/tiff

*   **padThumbnail**

    Allows one to enable padding of thumbnail products so it matches the size.

*   **transparent**

    Enables transparent output if the **outputFormat** supports it    """)
   @ApiImplicitParams([
          @ApiImplicitParam(name = 'images[0].file', value = 'filename', defaultValue = '', paramType = 'query', dataType = 'string', required=true),
          @ApiImplicitParam(name = 'images[0].entry', value = 'Image entry in the file', defaultValue = '', paramType = 'query', dataType = 'integer', required=false),
          @ApiImplicitParam(name = 'operation', value = '', defaultValue = 'none', allowableValues= "chip", paramType = 'query', dataType = 'string', required=false),
          @ApiImplicitParam(name = 'nullPixelFlip', value = 'Flip interior null pixels to valid', allowableValues="true,false", defaultValue="", paramType = 'query', dataType = 'boolean', required=false),
          @ApiImplicitParam(name = 'brightness', value = 'Brightness Operation',defaultValue="", paramType = 'query', dataType = 'float', required=false),
          @ApiImplicitParam(name = 'contrast', value = 'Contrast Operation',defaultValue="",  paramType = 'query', dataType = 'float', required=false),
          @ApiImplicitParam(name = 'sharpenMode', value = '', defaultValue = '', allowableValues="light,heavy",paramType = 'query', dataType = 'string', required=false),
          @ApiImplicitParam(name = 'thumbnailResolution', value = '', defaultValue = '',  paramType = 'query', dataType = 'integer', required=false),
          @ApiImplicitParam(name = 'cutBboxXywh', value = 'Cut image box separated by commas: <x>,<y>,<width>,<height>', defaultValue = '',  paramType = 'query', dataType = 'string', required=false),
          @ApiImplicitParam(name = 'rrds', value = 'Reduced resolution', defaultValue = '0',  paramType = 'query', dataType = 'integer', required=false),
          @ApiImplicitParam(name = 'histOp', value = 'Histogram Operation',defaultValue = '',allowableValues="none,auto-minmax,auto-percentile,std-stretch-1,std-stretch-2,std-stretch-3", paramType = 'query', dataType = 'string', required=false),
          @ApiImplicitParam(name = 'histCenter', value = 'Histogram Center Calculation',defaultValue = '',allowableValues="true,false", paramType = 'query', dataType = 'boolean', required=false),
          @ApiImplicitParam(name = 'outputRadiometry', value = 'Output radiometry', defaultValue = 'ossim_uint8', allowableValues="ossim_uint8,ossim_uint11,ossim_uint16,ossim_sint16,ossim_float32,ossim_float64", paramType = 'query', dataType = 'string', required=true),
          @ApiImplicitParam(name = 'bands', value = 'Bands', defaultValue = '',  paramType = 'query', dataType = 'string', required=false),
          @ApiImplicitParam(name = 'resamplerFilter', value = 'Which resampling engine to use', defaultValue = '',  allowableValues= "nearest-neighbor, bilinear, cubic, gaussian, blackman, bspline, hanning, hamming, hermite, mitchell, quadratic, sinc, magic", paramType = 'query', dataType = 'string', required=false),
          @ApiImplicitParam(name = 'outputFormat', value = 'Output format', defaultValue = '',  allowableValues= "image/jpeg,image/png,image/gif,image/tiff", paramType = 'query', dataType = 'string', required=false),
          @ApiImplicitParam(name = 'keepBands', value = 'Determine if we auto adjust bands or not', defaultValue = "false", paramType = 'query', dataType = 'boolean', required=false),
          @ApiImplicitParam(name = 'padThumbnail', value = 'Add padding to the output to make it square', defaultValue = "false", paramType = 'query', dataType = 'boolean', required=false),
          @ApiImplicitParam(name = 'transparent', value = 'Enable transparent if the outputFormat supports it', defaultValue = "true", paramType = 'query', dataType = 'boolean', required=false),
   ])
   def chip(){
      ChipperCommand command = new ChipperCommand()
      def json = request.JSON

      if(json)
      {
         json.operation="chip"
         bindData(command, BindUtil.fixParamNames(ChipperCommand, json))
      }
      else
      {
         params.operation="chip"
         bindData(command, BindUtil.fixParamNames(ChipperCommand, params))
      }

      def result = chipperService.getTile(command)
      response.contentType = result.contentType
      if(result.status != null) response.status        = result.status
      if(result.contentType) response.contentType      = result.contentType
      if(result.buffer?.length) response.contentLength = result.buffer.length

       def outputStream = null
       try{
          outputStream = response.getOutputStream()
          outputStream << result.buffer
       }
       catch(e)
       {
          log.error(e.toString())
       }
       finally{
          if(outputStream!=null)
          {
             try{
                outputStream.close()
             }
             catch(e)
             {
                log.debug(e.toString())
             }
          }
       }
   }

  @ApiOperation(value = "Get ortho chip from the passed in image file name",
                produces="image/png,image/jpeg,image/gif,image/tiff,text/plain",
                httpMethod="GET",
                notes = """
*   **images**

    Is an array of objects of the form images[0].file = **filename** and an optional images[0].entry = **entry**

*   **operation**

    Is fixed to the value ortho

*   **nullPixelFlip**

    Flips null interior pixels to valid

*   **brightness**

    Allows one to control the brightness of the image. This is expressed as a normalized value between -1 and 1.

*   **contrast**

    Allows one to control the contrast of an image. This is a multiplier.

*   **sharpenMode**

    Sharpen mode can take on the values none, light, or heavy.

*   **thumbnailResolution**

    Specify the resolution of the thumbnail. The valie is assumed square so only one dimension is needed. Setting the value to 512 will output to a 512x512 image.

*   **cutWidth**

    This is usually specified in conjuction with a geo spatial cut bos such as **cutWmsBbox** where the cutWidth is specified in pixels

*   **cutHeight**

    This is usually specified in conjuction with a geo spatial cut bos such as **cutWmsBbox** where the cutHeight is specified in pixels

*   **cutWmsBbox**

    Is a comma separated list of floating point values in the form of **minLon**,**minLat**,**maxLon**,**maxLat** and the values depend on the SRS projection used If its 3857 then it's in meters **minX**,**minY**,**maxX**,**maxY**

*   **histOp**

    Histogram operations used can be none, auto-minmax, auto-percentile, std-stretch-1, std-stretch-2, or std-stretch-3

*   **histCenter**

    Histogram should be calculated based on the center of the request

*   **srs**

    This is the SRS code. This can be EPSG:4326 (Geographic) EPSG:3857 (Global Mercator/Google mercator)

*   **outputRadiometry**

    Defines the output radiometry.

*   **bands**

    This defines an output band list that is a comma separated value of integers with band index 1's based. So a value of 1,1,1 will output three bands with all being the first band of the input image. If you have a 3 band input image you can reverse them by doing 3,2,1. Commas are required.

*   **resamplerFilter**

    We have exposed different filtering capabilities. Values can be any of the following nearest-neighbor, bilinear, cubic, gaussian, blackman, bspline, hanning, hamming, hermite, mitchell, quadratic, sinc, magic

*   **outputFormat**

    Output format can be image/jpeg, image/png, image/gif, or image/tiff

*   **keepBands**

    This is a flag that will allow the bands to be kept as specified. So if you wnat a 10 band output then you must void setUp() the **keepBands** to true and then specify the **outputFormat** to be image/tiff

*   **padThumbnail**

    Allows one to enable padding of thumbnail products so it matches the size.

*   **transparent**

    Enables transparent output if the **outputFormat** supports it    """)
   @ApiImplicitParams([
          @ApiImplicitParam(name = 'images[0].file', value = 'filename', defaultValue = '', paramType = 'query', dataType = 'string', required=true),
          @ApiImplicitParam(name = 'images[0].entry', value = 'Image entry in the file', defaultValue = '', paramType = 'query', dataType = 'integer', required=false),
          @ApiImplicitParam(name = 'operation', value = '', defaultValue = 'none', allowableValues= "ortho", paramType = 'query', dataType = 'string', required=false),
          @ApiImplicitParam(name = 'nullPixelFlip', value = 'Flip interior null pixels to valid', allowableValues="true,false", defaultValue="", paramType = 'query', dataType = 'boolean', required=false),
          @ApiImplicitParam(name = 'brightness', value = 'Brightness Operation',defaultValue="0.0", paramType = 'query', dataType = 'float', required=false),
          @ApiImplicitParam(name = 'contrast', value = 'Contrast Operation',defaultValue="1.0",  paramType = 'query', dataType = 'float', required=false),
          @ApiImplicitParam(name = 'sharpenMode', value = '', defaultValue = '', allowableValues="light,heavy",paramType = 'query', dataType = 'string', required=false),
          @ApiImplicitParam(name = 'thumbnailResolution', value = '', defaultValue = '',  paramType = 'query', dataType = 'integer', required=false),
          @ApiImplicitParam(name = 'cutWidth', value = 'Cut width in pixels', defaultValue = '',  paramType = 'query', dataType = 'integer', required=false),
          @ApiImplicitParam(name = 'cutHeight', value = 'Cut height in pixels', defaultValue = '',  paramType = 'query', dataType = 'integer', required=false),
          @ApiImplicitParam(name = 'cutWmsBbox', value = 'Cut wms bbox format', defaultValue = '',  paramType = 'query', dataType = 'string', required=false),
          @ApiImplicitParam(name = 'histOp', value = 'Histogram Operation',defaultValue = '',allowableValues="none,auto-minmax,auto-percentile,std-stretch-1,std-stretch-2,std-stretch-3", paramType = 'query', dataType = 'string', required=false),
          @ApiImplicitParam(name = 'histCenter', value = 'Histogram Center Calculation',defaultValue = '',allowableValues="true,false", paramType = 'query', dataType = 'boolean', required=false),
          @ApiImplicitParam(name = 'srs', value = 'srs', defaultValue = '',  allowableValues="EPSG:4326, EPSG:3857", paramType = 'query', dataType = 'string', required=false),
          @ApiImplicitParam(name = 'outputRadiometry', value = 'Output radiometry', defaultValue = 'ossim_uint8', allowableValues="ossim_uint8,ossim_uint11,ossim_uint16,ossim_sint16,ossim_float32,ossim_float64", paramType = 'query', dataType = 'string', required=true),
          @ApiImplicitParam(name = 'bands', value = 'Bands', defaultValue = '',  paramType = 'query', dataType = 'string', required=false),
          @ApiImplicitParam(name = 'resamplerFilter', value = 'Which resampling engine to use', defaultValue = '',  allowableValues= "nearest-neighbor, bilinear, cubic, gaussian, blackman, bspline, hanning, hamming, hermite, mitchell, quadratic, sinc, magic", paramType = 'query', dataType = 'string', required=false),
          @ApiImplicitParam(name = 'outputFormat', value = 'Output format', defaultValue = '',  allowableValues= "image/jpeg,image/png,image/gif,image/tiff", paramType = 'query', dataType = 'string', required=false),
          @ApiImplicitParam(name = 'keepBands', value = 'Determine if we auto adjust bands or not', defaultValue = "false", paramType = 'query', dataType = 'boolean', required=false),
          @ApiImplicitParam(name = 'padThumbnail', value = 'Add padding to the output to make it square', defaultValue = "false", paramType = 'query', dataType = 'boolean', required=false),
          @ApiImplicitParam(name = 'transparent', value = 'Enable transparent if the outputFormat supports it', defaultValue = "true", paramType = 'query', dataType = 'boolean', required=false),
   ])
   def ortho(){
      ChipperCommand command = new ChipperCommand()
      def json = request.JSON

      if(json)
      {
         json.operation="ortho"
         bindData(command, BindUtil.fixParamNames(ChipperCommand, json))
      }
      else
      {
         params.operation="ortho"
         bindData(command, BindUtil.fixParamNames(ChipperCommand, params))
      }
      def result = chipperService.getTile(command)
      response.contentType = result.contentType
      if(result.status != null) response.status        = result.status
      if(result.contentType) response.contentType      = result.contentType
      if(result.buffer?.length) response.contentLength = result.buffer.length

       def outputStream = null
       try{
          outputStream = response.getOutputStream()
          outputStream << result.buffer
       }
       catch(e)
       {
          log.error(e.toString())
       }
       finally{
          if(outputStream!=null)
          {
             try{
                outputStream.close()
             }
             catch(e)
             {
                log.debug(e.toString())
             }
          }
       }
   }
}
