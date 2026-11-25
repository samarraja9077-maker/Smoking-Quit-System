import fs from 'node:fs';
import path from 'node:path';
import express from 'express';
import { createServer as createViteServer } from 'vite';

const PORT = 3000;
const HOST = '0.0.0.0';

function resolveApkPath(): string | null {
  const candidates = [
    path.resolve(process.cwd(), 'app/build/outputs/apk/debug/app-debug.apk'),
    path.resolve(process.cwd(), 'public/SmokingQuitSystem-debug.apk'),
  ];
  for (const filePath of candidates) {
    if (fs.existsSync(filePath)) {
      return filePath;
    }
  }
  return null;
}

async function startServer() {
  const app = express();

  const sendApkHandler = (_req: express.Request, res: express.Response) => {
    const apkPath = resolveApkPath();
    if (!apkPath) {
      res.status(404).send('Android APK not found. Please run the Gradle build first.');
      return;
    }
    const stat = fs.statSync(apkPath);
    res.setHeader('Content-Type', 'application/vnd.android.package-archive');
    res.setHeader('Content-Disposition', 'attachment; filename="SmokingQuitSystem-debug.apk"');
    res.setHeader('Content-Length', stat.size.toString());
    res.setHeader('X-Content-Type-Options', 'nosniff');
    res.setHeader('Cache-Control', 'no-store');
    fs.createReadStream(apkPath).pipe(res);
  };

  app.get('/SmokingQuitSystem-debug.apk', sendApkHandler);
  app.get('/download/apk', sendApkHandler);

  app.get('/apk', (_req, res) => {
    const apkPath = resolveApkPath();
    const sizeMb = apkPath ? (fs.statSync(apkPath).size / (1024 * 1024)).toFixed(1) : '16.6';
    res.setHeader('Content-Type', 'text/html; charset=utf-8');
    res.send(`<!doctype html>
<html lang="en">
<head>
  <meta charset="UTF-8" />
  <meta name="viewport" content="width=device-width, initial-scale=1.0" />
  <title>Download Smoking Quit System APK</title>
  <style>
    body { margin:0; font-family: system-ui, -apple-system, sans-serif; background:#08130F; color:#F2F7F3; display:flex; min-height:100vh; align-items:center; justify-content:center; padding:24px; box-sizing:border-box; }
    .card { width:100%; max-width:420px; background:#12231B; border-radius:24px; padding:28px; text-align:center; border:1px solid #244B36; }
    .tag { color:#35D07F; font-size:12px; font-weight:700; letter-spacing:1.4px; }
    h1 { font-size:24px; margin:10px 0 6px; }
    p { color:#9FB7A9; font-size:14px; line-height:1.5; margin:0 0 20px; }
    .btn { display:block; width:100%; box-sizing:border-box; padding:16px; border-radius:16px; background:#35D07F; color:#08130F; font-weight:700; font-size:16px; text-decoration:none; }
    .note { margin-top:16px; font-size:12px; color:#9FB7A9; text-align:left; background:#08130F; padding:14px; border-radius:14px; }
  </style>
</head>
<body>
  <div class="card">
    <div style="font-size:44px;margin-bottom:8px;">📱</div>
    <div class="tag">SMOKING QUIT SYSTEM • NATIVE ANDROID</div>
    <h1>Download Android APK</h1>
    <p>Installable Android Debug APK (${sizeMb} MB) built from Kotlin, Jetpack Compose &amp; Room.</p>
    <a class="btn" href="/download/apk" download="SmokingQuitSystem-debug.apk">Download APK (${sizeMb} MB)</a>
    <div class="note">
      <strong>If the file opens instead of downloading:</strong><br/>
      Tap the <strong>⋮</strong> menu in the top-right corner and choose <strong>Open in Chrome / Open in browser</strong>, then tap Download again and allow <em>Install unknown apps</em>.
    </div>
  </div>
</body>
</html>`);
  });

  if (process.env.NODE_ENV !== 'production') {
    const vite = await createViteServer({
      server: { middlewareMode: true },
      appType: 'spa',
    });
    app.use(vite.middlewares);
  } else {
    const distPath = path.resolve(process.cwd(), 'dist');
    app.use(express.static(distPath));
    app.use((_req, res) => {
      res.sendFile(path.join(distPath, 'index.html'));
    });
  }

  app.listen(PORT, HOST, () => {
    console.log(`Server listening on http://${HOST}:${PORT}`);
  });
}

startServer();
