
namespace :jisop do
  task :compile do
    cd "jisop" do
      opt = "-quiet"
      if ENV["windir"] 
        sh "ant.bat #{opt}"
      else
        sh "ant #{opt}"
      end
    end
  end
end

