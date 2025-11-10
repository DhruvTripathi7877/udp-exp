# VPN Multicast Fix Instructions

## Problem
Your VPN (`utun4`) is intercepting multicast traffic and routing it through the tunnel, which doesn't support multicast properly, causing `NoRouteToHostException`.

## Current Routing Issue
```
224.0.0/4    utun4   <- VPN taking priority (WRONG)
224.0.0/4    en0     <- Wi-Fi interface (CORRECT)
```

---

## Solution 1: Remove VPN Multicast Route (TRY THIS FIRST)

### Step 1: Delete the VPN route
```bash
sudo route delete -net 224.0.0.0/4 -interface utun4
```

### Step 2: Verify only en0 remains
```bash
netstat -rn | grep "224.0.0"
```

You should see ONLY:
```
224.0.0/4    en0
```

### Step 3: Test your code
**Terminal 1:**
```bash
java MulticastServer
```

**Terminal 2:**
```bash
java MulticastClient
```

---

## Solution 2: If VPN Recreates the Route

Some VPNs automatically recreate the multicast route. If that happens, you have two options:

### Option A: Disconnect from VPN temporarily
Test your multicast code, then reconnect.

### Option B: Use a startup script
Create a script that deletes the VPN route every few seconds:
```bash
#!/bin/bash
while true; do
    sudo route delete -net 224.0.0.0/4 -interface utun4 2>/dev/null
    sleep 5
done
```

---

## Solution 3: Check macOS Firewall

Sometimes the firewall blocks multicast. Check with:
```bash
sudo /usr/libexec/ApplicationFirewall/socketfilterfw --getglobalstate
```

If enabled, temporarily disable or add Java to allowed apps:
```bash
sudo /usr/libexec/ApplicationFirewall/socketfilterfw --add /usr/bin/java
```

---

## What I Changed in Your Code

I updated `MulticastServerThread.java` to:
1. **Bind to en0's specific IP address** (`192.168.0.5`) - this bypasses some VPN routing
2. **Set TTL to 1** - keeps multicast local to your network
3. **Better error handling** - shows which interface/IP is being used

The output will now show:
```
Bound to: 192.168.0.5 on en0
```

---

## Troubleshooting

### Still getting "No route to host"?
1. Check if VPN recreated the route:
   ```bash
   netstat -rn | grep "224.0.0"
   ```

2. Try disconnecting VPN temporarily

3. Check if firewall is blocking

### "Operation not permitted" error?
Run with sudo or check System Preferences → Security & Privacy

### Client hanging?
Make sure you run the **server first**, then the client in a separate terminal.

---

## To Restore Normal Routing (After Testing)

If you want to restore the VPN multicast route:
```bash
sudo route add -net 224.0.0.0/4 -interface utun4
```

---

## Quick Test Command

Run both in sequence:
```bash
# Delete VPN route
sudo route delete -net 224.0.0.0/4 -interface utun4

# Verify
netstat -rn | grep "224.0.0"

# Test (open 2 terminals)
java MulticastServer    # Terminal 1
java MulticastClient    # Terminal 2
```

