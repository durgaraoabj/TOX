//var instrument = "SARTORIUS-WEIGTH";
//class LineBreakTransformer {
//    constructor() {
//      this.chunks = "";
//    }
//    
//    transform(chunk, controller) {
//        for(var i=0; i<=10000000;i++){
//        	for(var j=0; j<=10;j++){            	
//            }	
//        }
//        debugger;
//        console.log(chunk);
//        var str = chunk.trim();
//        if(str.includes('g')){
//        	var weight = str.substring(str.indexOf('+'));
//        	weight = weight.replace('g', '');
//        	weight = weight.replace(/ /g, '');
//        	weight = weight.trim();
//        	console.log("Weigth : " + weight);
//        	$("#"+weightBalanceDate).val(weight);
//        }	
//      }
//
//    flush(controller) {
//    	console.log("DATA[] : " + this.chunks);
//      controller.enqueue(this.chunks);
//    }
//}
//
//document.getElementById('connectButton').addEventListener('click', async () => {
//    try {
////    	
////    	const filter = { usbVendorId: 0x2341 };
//// const port1 = await navigator.serial.requestPort({ filters: [filter] });
//// alert(port1);
//      const port = await navigator.serial.requestPort();
//      console.log("req");
//      await port.open({ baudRate: 9600,       
//                        dataBits: 8,
//                        stopBits: 1,
//                        parity: "none",
//                        flowControl: "none",
//    });
//// debugger;
//    const {usbProductId, usbVendorId}=port.getInfo();
//    document.getElementById("output").innerHTML=usbProductId+" "+ " <br>"+usbVendorId;
//    
//      console.log("opanning");
//        
//                        
//
//                const writer = port.writable.getWriter();
//                 console.log("true is");
//                const data = new Uint8Array([0x32]); 
//            
//                await writer.write(data);
//                console.log("succ");
//
//        
//            writer.releaseLock();
//
//      while (port.readable) {
//        
//        const textDecoder = new TextDecoderStream();
//        const readableStreamClosed = port.readable.pipeTo(textDecoder.writable);
//        const reader = textDecoder.readable
//        .pipeThrough(new TransformStream(new LineBreakTransformer()))
//        .getReader();
//        console.log("portreadable");
//
//        try {
//          while (true) {
//            const { value, done } = await reader.read();
//            if (done) {
//              console.log("Canceled");
//              break;
//            }
//            console.log("tryfun");
//            console.log(value);
//          }
//        } catch (error) {
//          console.log("Error: Read");
//          console.log(error);
//        } finally {
//          reader.releaseLock();
//        }
//      }
//    } catch (error) {
//      console.log("Error: Open");
//      console.log(error);
//    }
//
//     
//  });