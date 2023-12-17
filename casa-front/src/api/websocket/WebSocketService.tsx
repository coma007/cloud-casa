import SockJS from "sockjs-client";
import * as Stomp from "stompjs";
import { WEBSOCKET } from "..";


// const [socket, setSocket] = useState<WebSocket | null>(null);
// useEffect(() => {
//   WebSocketService.createSocket(setSocket);
// }, []);
// useEffect(() => {
//   WebSocketService.defineSocket(socket, "NewTagRecordCreated", (message : string) => console.log(message));
// }, [socket]);


export const WebSocketService = {
	createSocket : function (topic : string, handleMessage : (message : any) => void) : WebSocket {
		let isLoaded = false;
		let socket = new SockJS("http://localhost:8080/socket");
		let stompClient = Stomp.over(socket);

		stompClient.connect({}, () =>{
			isLoaded = true;
			openSocket();
		});

		function openSocket() {
			if(isLoaded){
				stompClient!.subscribe(topic, (message) =>{
				handleMessage(JSON.parse(message.body));
				});
			}
		}
		return socket;
    }
  
}