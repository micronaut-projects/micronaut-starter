// App.js
import React from 'react';

function App({name, url}) {
    return (
        <html>
        <head>
            <title>Hello World!</title>
            <meta charSet="UTF-8"/>
        </head>
        <body>
            <p>Current URL is {url}</p>

            <button onClick={() => { alert('Hello from an event handler.'); }}>
                Hello {name}
            </button>
        </body>
        </html>
    );
}

export default App;
