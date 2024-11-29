@param org.mivirim.server.test.DemoModel model


<html>
    <head>
        <script src="https://unpkg.com/htmx.org@2.0.3"></script>
    </head>
    <body>
        <h1>Hello ${model.name}!</h1>
        <div>How are you? Really?</div>

        <!-- have a button POST a click via AJAX -->
        <button hx-post="/template/clicked" hx-swap="outerHTML">
            Click Me
        </button>

    </body>
</html>
