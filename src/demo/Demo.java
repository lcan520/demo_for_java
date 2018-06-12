package demo;

import demo.Proxy;
import net.sf.json.JSONObject;
import cmd.Registration;

import java.rmi.UnknownHostException;
import java.io.IOException;

import cmd.Message;
import cmd.RoundAction;

public class Demo {
	public static void main(String[] args) {
		String ip = args[1];
		int port = Integer.parseInt(args[2]);
		Proxy p = new Proxy(ip, port);
		p.connect();
		int team_id = Integer.parseInt(args[0]);
		String team_name = "219LI_3";
		Client client = new Client(team_id, team_name);

		/* registration */
		Registration r = new Registration(team_id, team_name);
		Message m = new Message("registration", r);
		p.send(m.toString());

		while (true) {
			String str = p.recieve();
			JSONObject json;
			try {
				json = JSONObject.fromObject(str);
			} catch (Exception e) {
				System.out.println(e.toString());
				continue;
			}

			String msg_name = json.getString("msg_name");
			if (msg_name.equals("leg_start")) {
				client.legStart(json.getJSONObject("msg_data"));
				continue;
			} else if (msg_name.equals("round")) {
				client.round(json.getJSONObject("msg_data"));
				RoundAction action = client.act();
				Message am = new Message("action", action);
				String send = am.toString();
				p.send(send);
				continue;
			} else if (msg_name.equals("leg_end")) {
				client.legEnd(json.getJSONObject("msg_data"));
				continue;
			} else if (msg_name.equals("game_over")) {
				System.out.println("game_over");
				break;
			} else {
				System.out.println("unkown message name " + msg_name);
			}
		}
	}
}
//	public static void main(String args[]) {
//		GamePlayer1 myThread1 = new GamePlayer1();
//		Thread thread1 = new Thread(myThread1);
//		thread1.start();
//		GamePlayer2 myThread2 = new GamePlayer2();
//		Thread thread2 = new Thread(myThread2);
//		thread2.start();
//	}
//
//}

//class GamePlayer1 implements Runnable{
//		@Override
//		public void run() {
//			Demo demo = new Demo();
//			demo.gemeTime(1001,"1001");
//			System.out.println("team_id:1001");
//		}
//	}
//
//
//class GamePlayer2 implements Runnable{
//
//		@Override
//		public void run() {
//			Demo demo = new Demo();
//			demo.gemeTime(1002,"1002");
//			System.out.println("team_id:1002");
//		}
//	}



