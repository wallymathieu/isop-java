task :default => [:compile]
task :compile do
  cd "jisop" do
    opt = "-quiet -f jisop.xml"
    if ENV["windir"] 
      sh "ant.bat #{opt}"
    else
      sh "ant #{opt}"
    end
  end
end

