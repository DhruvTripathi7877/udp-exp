#!/bin/bash
# Fix multicast routing for en0

echo "Fixing multicast routing..."
echo ""

# Delete any existing multicast routes
echo "1. Deleting existing multicast routes..."
sudo route delete -net 224.0.0.0/4 2>/dev/null
sudo route delete -net 224.0.0.0/4 -interface utun4 2>/dev/null
sudo route delete -net 224.0.0.0/4 -interface en0 2>/dev/null

echo ""
echo "2. Adding proper multicast route for en0..."
sudo route add -net 224.0.0.0/4 -interface en0

echo ""
echo "3. Current multicast routes:"
netstat -rn | grep "224.0.0"

echo ""
echo "Done! Now try running: java MulticastServer"

