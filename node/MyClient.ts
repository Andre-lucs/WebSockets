import * as net from 'net';
import {Socket} from "node:net";
import * as readline from "readline/promises";
import {read} from "node:fs";

class Client{
    private host: string;
    private port: number;
    private socket: net.Socket;
    constructor(host: string, port: number){
        this.host = host;
        this.port = port;
        this.socket = this.connect();
    }
    public connect(): Socket{
        let socket = new net.Socket();
        socket.connect(this.port, this.host, () => {
            console.log('Connected to server');
        });
        socket.on('data', (data: Buffer) => {
            console.log(`\nReceived: ${data.toString()}`);
        });

        socket.on('close', () => {
            console.log('Connection closed');
        });

        socket.on('error', (err: Error) => {
            console.error(`Error: ${err.message}`);
        });
        return socket;
    }

    public sendTextCommand(message: string) {
        const buffer  = Buffer.from(message);
        this.socket.write(buffer);
    }
    public sendBytes(bytes: Buffer){
        this.socket.write(bytes);
    }

    public getBytes(){
        this.socket.write(this.socket.read());
    }
    public exit(){
        this.sendTextCommand("EXIT")
        this.socket.end();
    }
    public hello(){
        this.sendBytes(Buffer.from('HELLO'));
    }

    public echo(message :string){
        this.sendTextCommand("ECHO "+message);
    }
}


const rl = readline.createInterface({
    input: process.stdin,
    output: process.stdout
});
let input: string;

async function readLine() {

    const answer = await rl.question('Type a command ex:hello, exit ', {
        signal: AbortSignal.timeout(20_000) // 10s timeout
    });
    rl.write(answer);
    if(answer.includes("hello")){
        client.hello();

    }else if(answer.includes("echo")){
        client.echo(answer.substring(answer.indexOf(" ")));
        return;
    }else if(answer.includes("exit")){
        client.exit();
        return;
    }
    await readLine();

}
async function main() {
    //client.getBytes();
    client.sendTextCommand("hello world");
    await readLine();
    rl.close();
}

const client = new Client('127.0.0.1', 9876);
main();