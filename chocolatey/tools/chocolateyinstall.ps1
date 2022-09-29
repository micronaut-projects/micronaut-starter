$version = '3.7.1'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '7DC6484F044045F1D033DAE186B7525736702EC4F5823C9F4988975DD4676D83'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
