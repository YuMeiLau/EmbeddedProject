

x = rand(200,1) ; y = rand(200,1) ; z = rand(200,1) ;

h = scatter3(x,y,z);
xlim([-1 2])
ylim([-1 2])
zlim([-1 2])

for i = 1:100
    x = rand(200,1) ;
    y = rand(200,1) ;
    z = rand(200,1) ;
    set(h,'XData',x,'YData',y,'ZData',z);
    drawnow
    pause(0.05)
end


