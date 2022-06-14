$version = '3.5.1'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'B9634072B08DE9A6EA72F876A17CD6BC06E23D32BE75448D1646BA121C8788D7'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
