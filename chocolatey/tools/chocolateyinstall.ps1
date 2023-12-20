$version = '4.2.2'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '4A94328B688441C6035B8B6654A7C79CD52633DECD187ED2D06869C9986834E6'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
