#!/bin/bash
# Monitor multicast routes in real-time

echo "Monitoring multicast routes (Ctrl+C to stop)..."
echo "If utun4 appears, your VPN is recreating the route!"
echo ""

while true; do
    clear
    date
    echo "=== Multicast Routes ==="
    netstat -rn | grep "224.0.0" | head -10
    sleep 2
done

