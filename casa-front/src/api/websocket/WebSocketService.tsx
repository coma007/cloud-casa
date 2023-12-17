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
	isLoaded: false,
	stompClient: null as Stomp.Client | null,
	stompSubscription: null as Stomp.Subscription | null,

	createSocket : function (topic : string, handleMessage : (message : any) => void) {
		let socket = new SockJS("http://localhost:8080/socket");
		this.stompClient = Stomp.over(socket);
		
		this.stompClient.connect({}, () =>{
			this.isLoaded = true;
			this.openSocket(topic, handleMessage);
		});
	},

	openSocket : function(topic : string, handleMessage : (message : any) => void) {
		if (this.stompClient && this.stompClient.connected) {
			if (this.stompSubscription) {
				this.stompSubscription.unsubscribe();
			}
			this.stompSubscription = this.stompClient!.subscribe(topic, (message) =>{
				handleMessage(JSON.parse(message.body));
			});
		}
	},

	unsubscribe : function() {
		if (this.stompSubscription) {
			this.stompSubscription.unsubscribe();
		}
		this.stompSubscription = null
	}

}